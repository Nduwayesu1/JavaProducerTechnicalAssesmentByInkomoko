package com.Inkomoko.Integration.producer;

import com.Inkomoko.Integration.model.Product;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Component
public class InventoryProducer extends AbstractProducer<Product> {

    private final ProductService productService;

    // Change KafkaTemplate<String, String> to KafkaTemplate<String, Object>
    public InventoryProducer(KafkaTemplate<String, Object> kafkaTemplate, // Changed here
                             RetryTemplate retryTemplate,
                             ObjectMapper objectMapper,
                             ProductService productService) {
        super(kafkaTemplate, retryTemplate, objectMapper);
        this.productService = productService;
    }

    @Override
    public String getTopicName() {
        return "inventory_data";
    }

    @Override
    public String getSourceName() {
        return "Inventory";
    }

    @Override
    @Scheduled(fixedRate = 30000) // Run every 30 seconds
    public void produceData() {
        try {
            logger.info("Fetching data from {}", getSourceName());
            List<Product> products = productService.fetchProducts();

            for (Product product : products) {
                String jsonData = convertToJson(product);
                sendMessage(jsonData);
            }

            logger.info("Successfully processed {} products from {}",
                    products.size(), getSourceName());

        } catch (Exception e) {
            logger.error("Failed to produce data from {}: {}", getSourceName(), e.getMessage(), e);
        }
    }
}
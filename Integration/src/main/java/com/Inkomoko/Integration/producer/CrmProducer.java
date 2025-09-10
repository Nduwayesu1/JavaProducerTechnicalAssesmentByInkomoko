package com.Inkomoko.Integration.producer;

import com.Inkomoko.Integration.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Component
public class CrmProducer extends AbstractProducer<Customer> {

    private final CustomerService customerService;

    @Value("${producer.schedule.enabled:true}")
    private boolean schedulingEnabled;

    // Change KafkaTemplate<String, String> to KafkaTemplate<String, Object>
    public CrmProducer(KafkaTemplate<String, Object> kafkaTemplate, // Changed here
                       RetryTemplate retryTemplate,
                       ObjectMapper objectMapper,
                       CustomerService customerService) {
        super(kafkaTemplate, retryTemplate, objectMapper);
        this.customerService = customerService;
    }

    @Override
    public String getTopicName() {
        return "customer_data";
    }

    @Override
    public String getSourceName() {
        return "CRM";
    }

    @Override
    @Scheduled(fixedRate = 30000) // Run every 30 seconds
    public void produceData() {
        try {
            logger.info("Fetching data from {}", getSourceName());
            List<Customer> customers = customerService.fetchCustomers();

            for (Customer customer : customers) {
                String jsonData = convertToJson(customer);
                sendMessage(jsonData);
            }

            logger.info("Successfully processed {} customers from {}",
                    customers.size(), getSourceName());

        } catch (Exception e) {
            logger.error("Failed to produce data from {}: {}", getSourceName(), e.getMessage(), e);
        }
    }
}
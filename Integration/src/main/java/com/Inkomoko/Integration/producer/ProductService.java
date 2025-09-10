package com.Inkomoko.Integration.producer;

import com.Inkomoko.Integration.model.Product;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final RestTemplate restTemplate;

    @Value("${app.inventory.base-url:http://localhost:8080}")
    private String inventoryBaseUrl;

    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retryable(maxAttempts = 3, backoff = @org.springframework.retry.annotation.Backoff(delay = 2000))
    public List<Product> fetchProducts() {
        logger.info("Fetching products from Inventory API: {}/products", inventoryBaseUrl);

        try {
            // Call your existing Product API
            Product[] products = restTemplate.getForObject(inventoryBaseUrl + "/products", Product[].class);

            if (products != null) {
                logger.info("Successfully fetched {} products from Inventory API", products.length);
                return Arrays.asList(products);
            } else {
                logger.warn("No products found or empty response from Inventory API");
                return Arrays.asList();
            }

        } catch (Exception e) {
            logger.error("Failed to fetch products from Inventory API: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch products from Inventory API", e);
        }
    }
}
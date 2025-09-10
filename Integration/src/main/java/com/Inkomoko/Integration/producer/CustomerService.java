package com.Inkomoko.Integration.producer;

import com.Inkomoko.Integration.model.Customer;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private final RestTemplate restTemplate;

    @Value("${app.crm.base-url:http://localhost:8080}")
    private String crmBaseUrl;

    public CustomerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retryable(maxAttempts = 3, backoff = @org.springframework.retry.annotation.Backoff(delay = 2000))
    public List<Customer> fetchCustomers() {
        logger.info("Fetching customers from CRM API: {}/customers", crmBaseUrl);

        try {
            // Call your existing Customer API
            Customer[] customers = restTemplate.getForObject(crmBaseUrl + "/customers", Customer[].class);

            if (customers != null) {
                logger.info("Successfully fetched {} customers from CRM API", customers.length);
                return Arrays.asList(customers);
            } else {
                logger.warn("No customers found or empty response from CRM API");
                return Arrays.asList();
            }

        } catch (Exception e) {
            logger.error("Failed to fetch customers from CRM API: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch customers from CRM API", e);
        }
    }
}
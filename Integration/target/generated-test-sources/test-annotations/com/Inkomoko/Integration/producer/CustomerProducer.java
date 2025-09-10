package com.Inkomoko.Integration.producer;

import com.Inkomoko.Integration.model.Customer;
import com.Inkomoko.Integration.model.Product;
import com.Inkomoko.Integration.producer.CustomerProducer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ProducerTest.TestConfig.class)
public class ProducerTest {

    @Autowired
    private CustomerProd customerProducer;

    @Autowired
    private InventoryProducer inventoryProducer;

    @Autowired
    private KafkaPublisher kafkaPublisher;

    @TestConfiguration
    @EnableRetry
    public static class TestConfig {

        @Bean
        public KafkaPublisher kafkaPublisher() {
            return Mockito.mock(KafkaPublisher.class);
        }

        @Bean
        public RetryTemplate retryTemplate() {
            return new RetryTemplate();
        }

        @Bean
        public CustomerProducer customerProducer(KafkaPublisher kafkaPublisher, RetryTemplate retryTemplate) {
            CustomerProducer producer = Mockito.spy(new CustomerProducer(kafkaPublisher, retryTemplate));

            // Mock fetchCustomers() to return sample data
            when(producer.fetchCustomers()).thenReturn(List.of(
                    new Customer("1", "Alice"),
                    new Customer("2", "Bob")
            ));
            return producer;
        }

        @Bean
        public InventoryProducer inventoryProducer(KafkaPublisher kafkaPublisher, RetryTemplate retryTemplate) {
            InventoryProducer producer = Mockito.spy(new InventoryProducer(kafkaPublisher, retryTemplate));

            // Mock fetchProducts() to return sample data
            when(producer.fetchProducts()).thenReturn(List.of(
                    new Product("101", "Laptop"),
                    new Product("102", "Phone")
            ));
            return producer;
        }
    }

    @Test
    public void testCustomerProducerSendsAllMessages() {
        customerProducer.produce();

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaPublisher, Mockito.times(customerProducer.fetchCustomers().size()))
                .publish(Mockito.eq("customer_data"), messageCaptor.capture());

        List<String> publishedMessages = messageCaptor.getAllValues();
        assertEquals(customerProducer.fetchCustomers().size(), publishedMessages.size());
    }

    @Test
    public void testInventoryProducerSendsAllMessages() {
        inventoryProducer.produce();

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaPublisher, Mockito.times(inventoryProducer.fetchProducts().size()))
                .publish(Mockito.eq("inventory_data"), messageCaptor.capture());

        List<String> publishedMessages = messageCaptor.getAllValues();
        assertEquals(inventoryProducer.fetchProducts().size(), publishedMessages.size());
    }
}

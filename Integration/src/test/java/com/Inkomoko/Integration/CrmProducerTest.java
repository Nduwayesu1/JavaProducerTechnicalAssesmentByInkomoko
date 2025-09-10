package com.Inkomoko.Integration;
import com.Inkomoko.Integration.model.Customer;
import com.Inkomoko.Integration.producer.CrmProducer;
import com.Inkomoko.Integration.producer.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(ports = 9092, topics = {"customer_data", "inventory_data"})
@ContextConfiguration(classes = TestKafkaConfig.class)
public class CrmProducerTest {

    @Autowired
    private CrmProducer crmProducer;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private RetryTemplate retryTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testProduceData_Success() throws Exception {
        // Arrange
        List<Customer> mockCustomers = Arrays.asList(
                new Customer("1", "John Doe", "john@example.com"),
                new Customer("2", "Jane Smith", "jane@example.com")
        );

        Mockito.when(customerService.fetchCustomers()).thenReturn(mockCustomers);

        // Act
        crmProducer.produceData();

        // Assert
        verify(customerService, times(1)).fetchCustomers();
        // Add assertions to verify Kafka messages were sent
    }

    @Test
    public void testProduceData_Exception() throws Exception {
        // Arrange
        Mockito.when(customerService.fetchCustomers())
                .thenThrow(new RuntimeException("Service unavailable"));

        // Act
        crmProducer.produceData();

        // Assert - should handle exception gracefully
        verify(customerService, times(1)).fetchCustomers();
    }
}
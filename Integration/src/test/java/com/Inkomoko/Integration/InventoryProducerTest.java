package com.Inkomoko.Integration;
import com.Inkomoko.Integration.model.Product;
import com.Inkomoko.Integration.producer.InventoryProducer;
import com.Inkomoko.Integration.producer.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(ports = 9092, topics = {"customer_data", "inventory_data"})
@ContextConfiguration(classes = TestKafkaConfig.class)
public class InventoryProducerTest {

    @Autowired
    private InventoryProducer inventoryProducer;

    @MockBean
    private ProductService productService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private RetryTemplate retryTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testProduceData_Success() throws Exception {
        // Arrange
        List<Product> mockProducts = Arrays.asList(
                new Product("1", "Laptop", (int) 999.99),
                new Product("2", "Mouse", (int) 25.99)
        );

        Mockito.when(productService.fetchProducts()).thenReturn(mockProducts);

        // Act
        inventoryProducer.produceData();

        // Assert
        verify(productService, times(1)).fetchProducts();
    }
}
package com.Inkomoko.Integration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

@TestConfiguration
public class TestKafkaConfig {

    @Bean
    public EmbeddedKafkaBroker embeddedKafkaBroker() {
        return new EmbeddedKafkaBroker(1, true, 2, "customer_data", "inventory_data")
                .kafkaPorts(9092)
                .brokerProperty("listeners", "PLAINTEXT://localhost:9092")
                .brokerProperty("advertised.listeners", "PLAINTEXT://localhost:9092");
    }
}
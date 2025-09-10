package com.Inkomoko.Integration.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractProducer<T> implements DataProducer<T> {

    protected final KafkaTemplate<String, Object> kafkaTemplate; // Changed to Object
    protected final RetryTemplate retryTemplate;
    protected final ObjectMapper objectMapper;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected AbstractProducer(KafkaTemplate<String, Object> kafkaTemplate, // Changed to Object
                               RetryTemplate retryTemplate,
                               ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.retryTemplate = retryTemplate;
        this.objectMapper = objectMapper;
    }

    protected void sendMessage(Object data) { // Changed to Object to accept both String and serialized objects
        retryTemplate.execute(context -> {
            try {
                kafkaTemplate.send(getTopicName(), data);
                logger.info("Successfully published data to topic: {}", getTopicName());
                return null;
            } catch (Exception e) {
                logger.error("Failed to publish data to topic: {} (attempt {})",
                        getTopicName(), context.getRetryCount() + 1, e);
                throw e;
            }
        });
    }

    protected String convertToJson(T data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
}
package com.Inkomoko.Integration.producer;

public interface DataProducer<T> {
    void produceData();
    String getTopicName();
    String getSourceName();
}
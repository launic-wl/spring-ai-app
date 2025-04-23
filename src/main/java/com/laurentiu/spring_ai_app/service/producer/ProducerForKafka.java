package com.laurentiu.spring_ai_app.service.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerForKafka {

    private static final String TOPIC = "prompt-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    // constructor bean injection
    public ProducerForKafka(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendMessageToTopic(String message) {

        kafkaTemplate.send(TOPIC, message);
    }

}

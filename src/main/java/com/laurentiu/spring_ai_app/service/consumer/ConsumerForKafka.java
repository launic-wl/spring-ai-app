package com.laurentiu.spring_ai_app.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.laurentiu.spring_ai_app.dto.ResponseWrapper;
import com.laurentiu.spring_ai_app.util.LlmResponseExtractor;
import com.laurentiu.spring_ai_app.websocket.ChatWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

@Service
public class ConsumerForKafka {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerForKafka.class);

    private CompletableFuture<String> currentResponse = new CompletableFuture<>();

    private final ChatWebSocketHandler chatWebSocketHandler;
    private final ObjectMapper objectMapper;

    public ConsumerForKafka(ChatWebSocketHandler chatWebSocketHandler,
                            ObjectMapper objectMapper) {
        this.chatWebSocketHandler = chatWebSocketHandler;
        this.objectMapper = objectMapper;
    }
    
    
    @KafkaListener(topics = "response-topic", groupId = "response-consumer-group")
    public void consumeLLMResponse(String receivedResponse) {

        logger.debug("Consumer receivedResponse = {}", receivedResponse);
        currentResponse.complete(receivedResponse);

        String[] parts = receivedResponse.split("\\|", 2);
        String sessionId = parts[0];
        String message = parts[1];

        logger.debug("sessionId = {}", sessionId);

        String response = LlmResponseExtractor.extractResponse(message);

        logger.debug("response = {}", response);

        String jsonResponse = "";
        try {
            jsonResponse = objectMapper.writeValueAsString(new ResponseWrapper(response));
            logger.debug("jsonResponse = {}", jsonResponse);
        }
        catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        chatWebSocketHandler.sendMessage(sessionId, jsonResponse);
    }

    public CompletableFuture<String> getResponseFuture() {

        currentResponse = new CompletableFuture<>();
        return currentResponse;
    }

    
}

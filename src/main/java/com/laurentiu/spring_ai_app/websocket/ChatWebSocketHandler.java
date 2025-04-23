package com.laurentiu.spring_ai_app.websocket;

import com.laurentiu.spring_ai_app.service.consumer.ConsumerForKafka;
import com.laurentiu.spring_ai_app.service.producer.ProducerForKafka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private final ProducerForKafka producerForKafka;


    // constructor bean injection
    public ChatWebSocketHandler(ProducerForKafka producerForKafka) {
        this.producerForKafka = producerForKafka;
    }


    public void sendMessage(String sessionId, String message) {
        WebSocketSession wsSession = sessions.get(sessionId);

        if(wsSession != null && wsSession.isOpen()) {

            try {
                wsSession.sendMessage(new TextMessage(message));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            throw new IllegalStateException("Session not found or closed for ID: " + sessionId);
        }
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = this.getSessionId(session);
        sessions.put(sessionId, session);
        logger.debug("WebSocket connected: {}", sessionId);
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String sessionId = this.getSessionId(session);
        String prompt = message.getPayload();
        // send message to Kafka prompt-topic
        producerForKafka.sendMessageToTopic(sessionId + "|" + prompt);
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String sessionId = this.getSessionId(session);
        sessions.remove(sessionId);
        logger.debug("WebSocket disconnected: {}", sessionId);
    }


    private String getSessionId(WebSocketSession session) {
        return Objects.requireNonNull(session.getUri()).getQuery().split("=")[1];
    }

}

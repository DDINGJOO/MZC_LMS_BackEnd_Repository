package com.mzc.lms.messaging.adapter.out.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mzc.lms.messaging.application.port.out.MessageEventPublisher;
import com.mzc.lms.messaging.domain.event.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageKafkaEventPublisher implements MessageEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(MessageKafkaEventPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topics.message-events:message-events}")
    private String messageEventsTopic;

    @Value("${kafka.topics.notification-requests:notification-requests}")
    private String notificationRequestsTopic;

    public MessageKafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate,
                                       SimpMessagingTemplate simpMessagingTemplate,
                                       ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(MessageEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);

            // Publish to Kafka for persistence and notification
            kafkaTemplate.send(messageEventsTopic, event.getRoomId(), message)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish message event: {}", ex.getMessage());
                        } else {
                            log.debug("Message event published: {}", event.getEventType());
                        }
                    });

            // Publish to notification service if it's a new message
            if ("MESSAGE_SENT".equals(event.getEventType())) {
                kafkaTemplate.send(notificationRequestsTopic, event.getRoomId(), message);
            }

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message event: {}", e.getMessage(), e);
        }
    }

    @Override
    public void publishToRoom(String roomId, MessageEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);

            // Publish via WebSocket to room subscribers
            simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, message);

            log.debug("Message published to room {}: {}", roomId, event.getEventType());

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message for WebSocket: {}", e.getMessage(), e);
        }
    }
}

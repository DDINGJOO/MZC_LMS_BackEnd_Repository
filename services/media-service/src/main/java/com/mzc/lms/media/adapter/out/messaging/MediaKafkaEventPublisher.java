package com.mzc.lms.media.adapter.out.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mzc.lms.media.application.port.out.MediaEventPublisher;
import com.mzc.lms.media.domain.event.MediaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MediaKafkaEventPublisher implements MediaEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topics.media-events:media-events}")
    private String mediaEventsTopic;

    @Override
    public void publish(MediaEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(mediaEventsTopic, event.getMediaId().toString(), message)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish media event: eventType={}, mediaId={}, error={}",
                                    event.getEventType(), event.getMediaId(), ex.getMessage());
                        } else {
                            log.info("Media event published: eventType={}, mediaId={}, partition={}",
                                    event.getEventType(), event.getMediaId(),
                                    result.getRecordMetadata().partition());
                        }
                    });

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize media event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to serialize media event", e);
        }
    }
}

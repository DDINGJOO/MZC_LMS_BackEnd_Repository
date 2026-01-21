package com.mzc.lms.media.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mzc.lms.media.application.port.in.MediaUseCase;
import com.mzc.lms.media.domain.event.MediaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MediaProcessingListener {

    private final MediaUseCase mediaUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topics.media-events:media-events}", groupId = "${spring.kafka.consumer.group-id:media-processor}")
    public void handleMediaEvent(String message) {
        try {
            MediaEvent event = objectMapper.readValue(message, MediaEvent.class);

            if ("MEDIA_UPLOADED".equals(event.getEventType())) {
                log.info("Received media upload event: mediaId={}", event.getMediaId());
                mediaUseCase.processMedia(event.getMediaId());
            }

        } catch (Exception e) {
            log.error("Failed to process media event: {}", e.getMessage(), e);
        }
    }
}

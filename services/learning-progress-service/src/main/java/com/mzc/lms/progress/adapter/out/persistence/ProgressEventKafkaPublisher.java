package com.mzc.lms.progress.adapter.out.persistence;

import com.mzc.lms.progress.application.port.out.ProgressEventPublisher;
import com.mzc.lms.progress.domain.event.LearningProgressEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProgressEventKafkaPublisher implements ProgressEventPublisher {

    private static final String TOPIC = "learning-progress-events";

    private final KafkaTemplate<String, LearningProgressEvent> kafkaTemplate;

    @Override
    public void publish(LearningProgressEvent event) {
        log.info("Publishing learning progress event: {} for progress: {}",
                event.getEventType(), event.getProgressId());

        kafkaTemplate.send(TOPIC, event.getEventId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish progress event: {}", event.getEventId(), ex);
                    } else {
                        log.debug("Successfully published progress event: {} to partition: {}",
                                event.getEventId(),
                                result.getRecordMetadata().partition());
                    }
                });
    }
}

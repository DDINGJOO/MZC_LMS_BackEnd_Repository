package com.mzc.lms.enrollment.adapter.out.persistence;

import com.mzc.lms.enrollment.application.port.out.EnrollmentEventPublisher;
import com.mzc.lms.enrollment.domain.event.EnrollmentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnrollmentEventKafkaPublisher implements EnrollmentEventPublisher {

    private static final String TOPIC = "enrollment-events";

    private final KafkaTemplate<String, EnrollmentEvent> kafkaTemplate;

    @Override
    public void publish(EnrollmentEvent event) {
        log.info("Publishing enrollment event: {} for enrollment: {}",
                event.getEventType(), event.getEnrollmentId());

        kafkaTemplate.send(TOPIC, event.getEventId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish enrollment event: {}", event.getEventId(), ex);
                    } else {
                        log.debug("Successfully published enrollment event: {} to partition: {}",
                                event.getEventId(),
                                result.getRecordMetadata().partition());
                    }
                });
    }
}

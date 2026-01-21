package com.mzc.lms.assessment.adapter.out.event;

import com.mzc.lms.assessment.application.port.out.AssessmentEventPublisher;
import com.mzc.lms.assessment.domain.event.AssessmentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AssessmentEventKafkaPublisher implements AssessmentEventPublisher {

    private static final String TOPIC = "assessment-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(AssessmentEvent event) {
        try {
            String key = event.getAssessmentId() != null ? event.getAssessmentId().toString() : "unknown";
            kafkaTemplate.send(TOPIC, key, event)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish assessment event: {}", event, ex);
                        } else {
                            log.debug("Assessment event published successfully: {}", event.getEventType());
                        }
                    });
        } catch (Exception e) {
            log.error("Error publishing assessment event: {}", event, e);
        }
    }
}

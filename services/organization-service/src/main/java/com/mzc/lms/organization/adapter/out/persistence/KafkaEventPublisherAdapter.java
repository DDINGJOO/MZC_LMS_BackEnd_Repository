package com.mzc.lms.organization.adapter.out.persistence;

import com.mzc.lms.organization.application.port.out.EventPublisherPort;
import com.mzc.lms.organization.domain.event.OrganizationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisherAdapter implements EventPublisherPort {

    private static final String ORGANIZATION_EVENTS_TOPIC = "organization-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(OrganizationEvent event) {
        log.info("Publishing organization event: type={}, entityType={}, entityId={}",
                event.getEventType(), event.getEntityType(), event.getEntityId());

        kafkaTemplate.send(ORGANIZATION_EVENTS_TOPIC, event.getEntityType() + "-" + event.getEntityId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish organization event: {}", ex.getMessage());
                    } else {
                        log.debug("Organization event published successfully");
                    }
                });
    }
}

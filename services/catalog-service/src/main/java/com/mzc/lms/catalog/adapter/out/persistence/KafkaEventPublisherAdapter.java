package com.mzc.lms.catalog.adapter.out.persistence;

import com.mzc.lms.catalog.application.port.out.EventPublisherPort;
import com.mzc.lms.catalog.domain.event.CatalogEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisherAdapter implements EventPublisherPort {

    private static final String TOPIC = "catalog-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(CatalogEvent event) {
        try {
            kafkaTemplate.send(TOPIC, event.getEntityType() + ":" + event.getEntityId(), event);
            log.info("Published event: {} for entity: {}:{}",
                    event.getEventType(), event.getEntityType(), event.getEntityId());
        } catch (Exception e) {
            log.error("Failed to publish event: {} for entity: {}:{}",
                    event.getEventType(), event.getEntityType(), event.getEntityId(), e);
        }
    }
}

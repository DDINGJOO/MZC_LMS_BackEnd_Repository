package com.mzc.lms.profile.adapter.out.persistence;

import com.mzc.lms.profile.application.port.out.EventPublisherPort;
import com.mzc.lms.profile.domain.event.ProfileUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisherAdapter implements EventPublisherPort {

    private static final String PROFILE_EVENTS_TOPIC = "profile-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishProfileUpdated(ProfileUpdatedEvent event) {
        log.info("Publishing profile updated event: userId={}, eventType={}",
                event.getUserId(), event.getEventType());

        kafkaTemplate.send(PROFILE_EVENTS_TOPIC, String.valueOf(event.getUserId()), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish profile updated event: {}", ex.getMessage());
                    } else {
                        log.debug("Profile updated event published successfully");
                    }
                });
    }
}

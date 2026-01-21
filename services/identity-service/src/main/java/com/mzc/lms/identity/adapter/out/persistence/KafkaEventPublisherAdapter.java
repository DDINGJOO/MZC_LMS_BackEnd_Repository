package com.mzc.lms.identity.adapter.out.persistence;

import com.mzc.lms.identity.application.port.out.EventPublisherPort;
import com.mzc.lms.identity.domain.event.UserAuthenticatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisherAdapter implements EventPublisherPort {

    private static final String USER_AUTHENTICATED_TOPIC = "user.authenticated";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishUserAuthenticated(UserAuthenticatedEvent event) {
        try {
            kafkaTemplate.send(USER_AUTHENTICATED_TOPIC, event.getUserId().toString(), event);
            log.debug("이벤트 발행 완료: topic={}, userId={}", USER_AUTHENTICATED_TOPIC, event.getUserId());
        } catch (Exception e) {
            log.error("이벤트 발행 실패: topic={}, error={}", USER_AUTHENTICATED_TOPIC, e.getMessage(), e);
        }
    }
}

package com.mzc.lms.identity.application.port.out;

import com.mzc.lms.identity.domain.event.UserAuthenticatedEvent;

/**
 * 이벤트 발행 Port
 */
public interface EventPublisherPort {

    void publishUserAuthenticated(UserAuthenticatedEvent event);
}

package com.mzc.lms.organization.application.port.out;

import com.mzc.lms.organization.domain.event.OrganizationEvent;

public interface EventPublisherPort {

    void publish(OrganizationEvent event);
}

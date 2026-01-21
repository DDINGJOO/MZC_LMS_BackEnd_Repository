package com.mzc.lms.profile.application.port.out;

import com.mzc.lms.profile.domain.event.ProfileUpdatedEvent;

public interface EventPublisherPort {

    void publishProfileUpdated(ProfileUpdatedEvent event);
}

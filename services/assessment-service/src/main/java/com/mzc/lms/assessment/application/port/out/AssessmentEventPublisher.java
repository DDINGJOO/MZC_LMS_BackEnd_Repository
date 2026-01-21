package com.mzc.lms.assessment.application.port.out;

import com.mzc.lms.assessment.domain.event.AssessmentEvent;

public interface AssessmentEventPublisher {

    void publish(AssessmentEvent event);
}

package com.mzc.lms.enrollment.application.port.out;

import com.mzc.lms.enrollment.domain.event.EnrollmentEvent;

public interface EnrollmentEventPublisher {

    void publish(EnrollmentEvent event);
}

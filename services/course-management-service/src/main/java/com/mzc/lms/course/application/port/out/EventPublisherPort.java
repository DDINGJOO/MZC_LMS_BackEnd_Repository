package com.mzc.lms.course.application.port.out;

import com.mzc.lms.course.domain.event.CourseEvent;

public interface EventPublisherPort {

    void publish(CourseEvent event);
}

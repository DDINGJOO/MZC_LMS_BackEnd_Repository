package com.mzc.lms.progress.application.port.out;

import com.mzc.lms.progress.domain.event.LearningProgressEvent;

public interface ProgressEventPublisher {

    void publish(LearningProgressEvent event);
}

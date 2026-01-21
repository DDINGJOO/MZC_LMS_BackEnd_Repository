package com.mzc.lms.media.application.port.out;

import com.mzc.lms.media.domain.event.MediaEvent;

public interface MediaEventPublisher {

    void publish(MediaEvent event);
}

package com.mzc.lms.messaging.application.port.out;

import com.mzc.lms.messaging.domain.event.MessageEvent;

public interface MessageEventPublisher {

    void publish(MessageEvent event);

    void publishToRoom(String roomId, MessageEvent event);
}

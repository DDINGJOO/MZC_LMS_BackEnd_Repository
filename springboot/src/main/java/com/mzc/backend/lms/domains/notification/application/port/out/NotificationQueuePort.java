package com.mzc.backend.lms.domains.notification.application.port.out;

import com.mzc.backend.lms.domains.notification.domain.model.BatchNotificationMessage;
import com.mzc.backend.lms.domains.notification.domain.model.NotificationMessage;

import java.util.Optional;

/**
 * 알림 큐 Port
 */
public interface NotificationQueuePort {

    /**
     * 단일 알림 메시지를 큐에 발행
     */
    void publish(NotificationMessage message);

    /**
     * 배치 알림 메시지를 큐에 발행
     */
    void publishBatch(BatchNotificationMessage message);

    /**
     * 큐에서 알림 메시지를 가져옴 (blocking)
     */
    Optional<NotificationMessage> dequeue(long timeoutSeconds);

    /**
     * 큐에서 배치 알림 메시지를 가져옴 (blocking)
     */
    Optional<BatchNotificationMessage> dequeueBatch(long timeoutSeconds);
}

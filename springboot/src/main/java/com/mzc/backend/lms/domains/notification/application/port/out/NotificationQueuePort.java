package com.mzc.backend.lms.domains.notification.application.port.out;

import com.mzc.backend.lms.domains.notification.adapter.out.queue.dto.BatchNotificationMessage;
import com.mzc.backend.lms.domains.notification.adapter.out.queue.dto.NotificationMessage;

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
}

package com.mzc.backend.lms.domains.notification.application.port.in;

import com.mzc.backend.lms.domains.notification.domain.model.BatchNotificationMessage;
import com.mzc.backend.lms.domains.notification.domain.model.NotificationMessage;

/**
 * 알림 처리 UseCase (Inbound Port)
 * 큐에서 받은 알림 메시지를 처리
 */
public interface NotificationProcessor {

    /**
     * 단일 알림 메시지 처리
     */
    void process(NotificationMessage message);

    /**
     * 배치 알림 메시지 처리
     */
    void processBatch(BatchNotificationMessage message);
}

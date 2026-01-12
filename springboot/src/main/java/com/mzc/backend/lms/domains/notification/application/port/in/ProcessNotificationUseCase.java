package com.mzc.backend.lms.domains.notification.application.port.in;

import com.mzc.backend.lms.domains.notification.adapter.out.queue.dto.BatchNotificationMessage;
import com.mzc.backend.lms.domains.notification.adapter.out.queue.dto.NotificationMessage;

/**
 * 알림 처리 UseCase (큐에서 소비된 메시지 처리)
 */
public interface ProcessNotificationUseCase {

    /**
     * 단일 알림 메시지 처리
     */
    void process(NotificationMessage message);

    /**
     * 배치 알림 메시지 처리
     */
    void processBatch(BatchNotificationMessage message);
}

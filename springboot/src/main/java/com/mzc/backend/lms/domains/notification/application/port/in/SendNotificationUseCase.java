package com.mzc.backend.lms.domains.notification.application.port.in;

import com.mzc.backend.lms.common.notification.event.NotificationEventType;

import java.util.List;

/**
 * 알림 발송 UseCase
 */
public interface SendNotificationUseCase {

    /**
     * 단일 알림 발송
     */
    void send(NotificationEventType eventType, Long senderId, Long recipientId,
              String title, String message);

    /**
     * 강의 관련 알림 발송
     */
    void sendForCourse(NotificationEventType eventType, Long senderId, Long recipientId,
                       Long courseId, String title, String message);

    /**
     * 엔티티 관련 알림 발송
     */
    void sendWithEntity(NotificationEventType eventType, Long senderId, Long recipientId,
                        String relatedEntityType, Long relatedEntityId, Long courseId,
                        String title, String message, String actionUrl);

    /**
     * 배치 알림 발송
     */
    void sendBatch(NotificationEventType eventType, Long senderId, List<Long> recipientIds,
                   Long courseId, String title, String message);

    /**
     * 엔티티 관련 배치 알림 발송
     */
    void sendBatchWithEntity(NotificationEventType eventType, Long senderId, List<Long> recipientIds,
                             String relatedEntityType, Long relatedEntityId, Long courseId,
                             String title, String message, String actionUrl);
}

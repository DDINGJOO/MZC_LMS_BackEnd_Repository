package com.mzc.backend.lms.domains.notification.application.port.in;

import com.mzc.backend.lms.domains.notification.adapter.in.web.dto.NotificationResponseDto;

/**
 * 알림 관리 UseCase (읽음 처리, 삭제)
 */
public interface ManageNotificationUseCase {

    /**
     * 알림 읽음 처리
     */
    NotificationResponseDto markAsRead(Long userId, Long notificationId);

    /**
     * 모든 알림 읽음 처리
     */
    int markAllAsRead(Long userId);

    /**
     * 알림 삭제
     */
    void deleteNotification(Long userId, Long notificationId);

    /**
     * 읽은 알림 일괄 삭제
     */
    int deleteReadNotifications(Long userId);

    /**
     * 모든 알림 삭제
     */
    int deleteAllNotifications(Long userId);
}

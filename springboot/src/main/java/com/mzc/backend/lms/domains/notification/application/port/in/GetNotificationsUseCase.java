package com.mzc.backend.lms.domains.notification.application.port.in;

import com.mzc.backend.lms.domains.notification.adapter.in.web.dto.NotificationCursorResponseDto;
import com.mzc.backend.lms.domains.notification.adapter.in.web.dto.NotificationResponseDto;

/**
 * 알림 조회 UseCase
 */
public interface GetNotificationsUseCase {

    /**
     * 커서 기반 알림 목록 조회
     */
    NotificationCursorResponseDto getNotifications(Long userId, Long cursor, int size);

    /**
     * 커서 기반 읽지 않은 알림 목록 조회
     */
    NotificationCursorResponseDto getUnreadNotifications(Long userId, Long cursor, int size);

    /**
     * 알림 상세 조회
     */
    NotificationResponseDto getNotification(Long userId, Long notificationId);

    /**
     * 읽지 않은 알림 개수 조회
     */
    long getUnreadCount(Long userId);
}

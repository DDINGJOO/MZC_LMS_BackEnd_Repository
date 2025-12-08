package com.mzc.backend.lms.domains.notification.service;

import com.mzc.backend.lms.domains.notification.dto.NotificationListResponseDto;
import com.mzc.backend.lms.domains.notification.dto.NotificationResponseDto;
import org.springframework.data.domain.Pageable;

/**
 * 알림 서비스 인터페이스
 */
public interface NotificationService {

    /**
     * 사용자의 알림 목록 조회
     *
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 알림 목록 응답
     */
    NotificationListResponseDto getNotifications(Long userId, Pageable pageable);

    /**
     * 사용자의 읽지 않은 알림 목록 조회
     *
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 알림 목록 응답
     */
    NotificationListResponseDto getUnreadNotifications(Long userId, Pageable pageable);

    /**
     * 알림 상세 조회
     *
     * @param userId 사용자 ID
     * @param notificationId 알림 ID
     * @return 알림 응답
     */
    NotificationResponseDto getNotification(Long userId, Long notificationId);

    /**
     * 알림 읽음 처리
     *
     * @param userId 사용자 ID
     * @param notificationId 알림 ID
     * @return 업데이트된 알림 응답
     */
    NotificationResponseDto markAsRead(Long userId, Long notificationId);

    /**
     * 모든 알림 읽음 처리
     *
     * @param userId 사용자 ID
     * @return 읽음 처리된 알림 개수
     */
    int markAllAsRead(Long userId);

    /**
     * 읽지 않은 알림 개수 조회
     *
     * @param userId 사용자 ID
     * @return 읽지 않은 알림 개수
     */
    long getUnreadCount(Long userId);

    /**
     * 알림 삭제
     *
     * @param userId 사용자 ID
     * @param notificationId 알림 ID
     */
    void deleteNotification(Long userId, Long notificationId);
}

package com.mzc.backend.lms.domains.notification.application.service;

import com.mzc.backend.lms.domains.notification.adapter.in.web.dto.NotificationResponseDto;
import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.Notification;
import com.mzc.backend.lms.domains.notification.application.port.in.ManageNotificationUseCase;
import com.mzc.backend.lms.domains.notification.application.port.out.NotificationRepositoryPort;
import com.mzc.backend.lms.domains.notification.exception.NotificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 알림 관리 서비스 (읽음 처리, 삭제)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationManagementService implements ManageNotificationUseCase {

    private final NotificationRepositoryPort notificationRepositoryPort;

    @Override
    public NotificationResponseDto markAsRead(Long userId, Long notificationId) {
        Notification notification = notificationRepositoryPort.findById(notificationId)
                .orElseThrow(() -> NotificationException.notificationNotFound(notificationId));

        validateRecipient(notification, userId);

        notification.markAsRead();
        notificationRepositoryPort.save(notification);

        log.debug("알림 읽음 처리: notificationId={}, userId={}", notificationId, userId);

        return NotificationResponseDto.from(notification);
    }

    @Override
    public int markAllAsRead(Long userId) {
        int updatedCount = notificationRepositoryPort.markAllAsReadByRecipientId(userId);
        log.info("모든 알림 읽음 처리: userId={}, count={}", userId, updatedCount);
        return updatedCount;
    }

    @Override
    public void deleteNotification(Long userId, Long notificationId) {
        Notification notification = notificationRepositoryPort.findById(notificationId)
                .orElseThrow(() -> NotificationException.notificationNotFound(notificationId));

        validateRecipient(notification, userId);

        notificationRepositoryPort.deleteById(notificationId);
        log.info("알림 삭제: notificationId={}, userId={}", notificationId, userId);
    }

    @Override
    public int deleteReadNotifications(Long userId) {
        int deletedCount = notificationRepositoryPort.deleteReadByRecipientId(userId);
        log.info("읽은 알림 삭제: userId={}, count={}", userId, deletedCount);
        return deletedCount;
    }

    @Override
    public int deleteAllNotifications(Long userId) {
        int deletedCount = notificationRepositoryPort.deleteAllByRecipientId(userId);
        log.info("모든 알림 삭제: userId={}, count={}", userId, deletedCount);
        return deletedCount;
    }

    private void validateRecipient(Notification notification, Long userId) {
        if (!notification.getRecipient().getId().equals(userId)) {
            throw NotificationException.notNotificationOwner(userId, notification.getId());
        }
    }
}

package com.mzc.backend.lms.domains.notification.application.service;

import com.mzc.backend.lms.domains.notification.adapter.in.web.dto.NotificationCursorResponseDto;
import com.mzc.backend.lms.domains.notification.adapter.in.web.dto.NotificationResponseDto;
import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.Notification;
import com.mzc.backend.lms.domains.notification.application.port.in.GetNotificationsUseCase;
import com.mzc.backend.lms.domains.notification.application.port.out.NotificationRepositoryPort;
import com.mzc.backend.lms.domains.notification.exception.NotificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 알림 조회 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryService implements GetNotificationsUseCase {

    private final NotificationRepositoryPort notificationRepositoryPort;

    @Override
    public NotificationCursorResponseDto getNotifications(Long userId, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size + 1);

        List<Notification> notifications;
        if (cursor == null) {
            notifications = notificationRepositoryPort.findByRecipientIdOrderByIdDesc(userId, pageable);
        } else {
            notifications = notificationRepositoryPort.findByRecipientIdAndIdLessThanOrderByIdDesc(
                    userId, cursor, pageable);
        }

        List<NotificationResponseDto> dtos = notifications.stream()
                .map(NotificationResponseDto::from)
                .collect(Collectors.toList());

        Long unreadCount = cursor == null
                ? notificationRepositoryPort.countUnreadByRecipientId(userId)
                : null;

        return NotificationCursorResponseDto.of(dtos, size, unreadCount);
    }

    @Override
    public NotificationCursorResponseDto getUnreadNotifications(Long userId, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size + 1);

        List<Notification> notifications;
        if (cursor == null) {
            notifications = notificationRepositoryPort.findUnreadByRecipientIdOrderByIdDesc(userId, pageable);
        } else {
            notifications = notificationRepositoryPort.findUnreadByRecipientIdAndIdLessThanOrderByIdDesc(
                    userId, cursor, pageable);
        }

        List<NotificationResponseDto> dtos = notifications.stream()
                .map(NotificationResponseDto::from)
                .collect(Collectors.toList());

        Long unreadCount = cursor == null
                ? notificationRepositoryPort.countUnreadByRecipientId(userId)
                : null;

        return NotificationCursorResponseDto.of(dtos, size, unreadCount);
    }

    @Override
    @Transactional
    public NotificationResponseDto getNotification(Long userId, Long notificationId) {
        Notification notification = notificationRepositoryPort.findById(notificationId)
                .orElseThrow(() -> NotificationException.notificationNotFound(notificationId));

        validateRecipient(notification, userId);

        if (notification.isUnread()) {
            notification.markAsRead();
            notificationRepositoryPort.save(notification);
        }

        return NotificationResponseDto.from(notification);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepositoryPort.countUnreadByRecipientId(userId);
    }

    private void validateRecipient(Notification notification, Long userId) {
        if (!notification.getRecipient().getId().equals(userId)) {
            throw NotificationException.notNotificationOwner(userId, notification.getId());
        }
    }
}

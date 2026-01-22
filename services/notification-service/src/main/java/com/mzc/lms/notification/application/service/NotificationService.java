package com.mzc.lms.notification.application.service;

import com.mzc.lms.notification.adapter.in.web.dto.NotificationSendRequest;
import com.mzc.lms.notification.application.port.in.NotificationUseCase;
import com.mzc.lms.notification.application.port.out.NotificationRepositoryPort;
import com.mzc.lms.notification.application.port.out.NotificationSenderPort;
import com.mzc.lms.notification.application.port.out.UserPreferenceRepositoryPort;
import com.mzc.lms.notification.domain.model.Notification;
import com.mzc.lms.notification.domain.model.NotificationChannel;
import com.mzc.lms.notification.domain.model.UserNotificationPreference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService implements NotificationUseCase {

    private final NotificationRepositoryPort notificationRepository;
    private final UserPreferenceRepositoryPort preferenceRepository;
    private final NotificationSenderPort notificationSender;

    @Override
    @Transactional
    public Notification sendNotification(NotificationSendRequest request) {
        log.info("Sending notification to user: {}", request.getUserId());

        // Check user preferences
        UserNotificationPreference preference = preferenceRepository.findByUserId(request.getUserId())
                .orElse(createDefaultPreference(request.getUserId()));

        // Check if channel is enabled
        if (!preference.isChannelEnabled(request.getChannel())) {
            log.info("Channel {} is disabled for user {}", request.getChannel(), request.getUserId());
            return null;
        }

        // Check quiet hours
        if (preference.isQuietHours(LocalTime.now())) {
            log.info("User {} is in quiet hours, skipping notification", request.getUserId());
            return null;
        }

        // Create and save notification
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .channel(request.getChannel())
                .title(request.getTitle())
                .content(request.getContent())
                .data(request.getData())
                .isRead(false)
                .build();

        Notification saved = notificationRepository.save(notification);

        // Send notification via appropriate channel
        sendViaChannel(saved, request);

        return saved;
    }

    @Override
    public List<Notification> getNotifications(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notification> notifications = notificationRepository.findByUserId(userId, pageable);
        return notifications.getContent();
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        log.info("Marking notification as read: {}", notificationId);
        notificationRepository.markAsRead(notificationId);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        log.info("Marking all notifications as read for user: {}", userId);
        notificationRepository.markAllAsReadByUserId(userId);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    @Override
    public Notification getNotification(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + id));
    }

    private void sendViaChannel(Notification notification, NotificationSendRequest request) {
        try {
            switch (notification.getChannel()) {
                case PUSH:
                    if (request.getDeviceToken() != null) {
                        notificationSender.sendPush(notification, request.getDeviceToken());
                    }
                    break;
                case EMAIL:
                    if (request.getEmail() != null) {
                        notificationSender.sendEmail(notification, request.getEmail());
                    }
                    break;
                case SMS:
                    if (request.getPhoneNumber() != null) {
                        notificationSender.sendSms(notification, request.getPhoneNumber());
                    }
                    break;
                case IN_APP:
                    // In-app notifications are already saved in DB
                    break;
            }
        } catch (Exception e) {
            log.error("Failed to send notification via {}: {}", notification.getChannel(), e.getMessage());
        }
    }

    private UserNotificationPreference createDefaultPreference(Long userId) {
        UserNotificationPreference preference = UserNotificationPreference.builder()
                .userId(userId)
                .build();

        // Enable all channels by default
        preference.enableChannel(NotificationChannel.IN_APP);
        preference.enableChannel(NotificationChannel.EMAIL);
        preference.enableChannel(NotificationChannel.PUSH);

        return preferenceRepository.save(preference);
    }
}

package com.mzc.lms.notification.application.port.in;

import com.mzc.lms.notification.adapter.in.web.dto.NotificationSendRequest;
import com.mzc.lms.notification.domain.model.Notification;

import java.util.List;

public interface NotificationUseCase {

    Notification sendNotification(NotificationSendRequest request);

    List<Notification> getNotifications(Long userId, int page, int size);

    void markAsRead(Long notificationId);

    void markAllAsRead(Long userId);

    long getUnreadCount(Long userId);

    Notification getNotification(Long id);
}

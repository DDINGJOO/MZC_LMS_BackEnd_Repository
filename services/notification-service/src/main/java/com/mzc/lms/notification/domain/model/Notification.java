package com.mzc.lms.notification.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private Long id;
    private Long userId;
    private NotificationType type;
    private NotificationChannel channel;
    private String title;
    private String content;
    private Map<String, Object> data;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;

    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }

    public boolean isUnread() {
        return !this.isRead;
    }
}

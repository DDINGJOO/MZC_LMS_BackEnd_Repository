package com.mzc.lms.notification.adapter.in.web.dto;

import com.mzc.lms.notification.domain.model.Notification;
import com.mzc.lms.notification.domain.model.NotificationChannel;
import com.mzc.lms.notification.domain.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

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

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .type(notification.getType())
                .channel(notification.getChannel())
                .title(notification.getTitle())
                .content(notification.getContent())
                .data(notification.getData())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .readAt(notification.getReadAt())
                .build();
    }

    public static List<NotificationResponse> from(List<Notification> notifications) {
        return notifications.stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }
}

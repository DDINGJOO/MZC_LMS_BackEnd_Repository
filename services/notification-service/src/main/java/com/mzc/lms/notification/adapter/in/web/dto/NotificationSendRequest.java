package com.mzc.lms.notification.adapter.in.web.dto;

import com.mzc.lms.notification.domain.model.NotificationChannel;
import com.mzc.lms.notification.domain.model.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSendRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    @NotNull(message = "Notification channel is required")
    private NotificationChannel channel;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private Map<String, Object> data;

    // Channel-specific fields
    private String deviceToken;  // For PUSH
    private String email;         // For EMAIL
    private String phoneNumber;   // For SMS
}

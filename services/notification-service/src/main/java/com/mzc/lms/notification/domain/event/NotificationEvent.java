package com.mzc.lms.notification.domain.event;

import com.mzc.lms.notification.domain.model.NotificationChannel;
import com.mzc.lms.notification.domain.model.NotificationType;
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
public class NotificationEvent {

    private String eventId;
    private Long userId;
    private NotificationType type;
    private NotificationChannel channel;
    private String title;
    private String content;
    private Map<String, Object> data;
    private LocalDateTime occurredAt;

    public static NotificationEvent create(
            Long userId,
            NotificationType type,
            NotificationChannel channel,
            String title,
            String content,
            Map<String, Object> data
    ) {
        return NotificationEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .userId(userId)
                .type(type)
                .channel(channel)
                .title(title)
                .content(content)
                .data(data)
                .occurredAt(LocalDateTime.now())
                .build();
    }
}

package com.mzc.lms.notification.adapter.out.persistence.entity;

import com.mzc.lms.notification.domain.model.NotificationChannel;
import com.mzc.lms.notification.domain.model.NotificationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_user_created", columnList = "user_id,created_at"),
        @Index(name = "idx_user_read", columnList = "user_id,is_read")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 20)
    private NotificationChannel channel;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "data", columnDefinition = "JSON")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> data;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Builder
    public NotificationEntity(Long userId, NotificationType type, NotificationChannel channel,
                              String title, String content, Map<String, Object> data) {
        this.userId = userId;
        this.type = type;
        this.channel = channel;
        this.title = title;
        this.content = content;
        this.data = data;
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }
}

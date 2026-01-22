package com.mzc.lms.notification.adapter.out.persistence.entity;

import com.mzc.lms.notification.domain.model.NotificationChannel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_notification_preferences")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserNotificationPreferenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "enabled_notification_channels", joinColumns = @JoinColumn(name = "preference_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "channel")
    private Set<NotificationChannel> enabledChannels = new HashSet<>();

    @Column(name = "quiet_hours_start")
    private LocalTime quietHoursStart;

    @Column(name = "quiet_hours_end")
    private LocalTime quietHoursEnd;

    @Builder
    public UserNotificationPreferenceEntity(Long userId, Set<NotificationChannel> enabledChannels,
                                            LocalTime quietHoursStart, LocalTime quietHoursEnd) {
        this.userId = userId;
        this.enabledChannels = enabledChannels != null ? enabledChannels : new HashSet<>();
        this.quietHoursStart = quietHoursStart;
        this.quietHoursEnd = quietHoursEnd;
    }

    public void updateQuietHours(LocalTime start, LocalTime end) {
        this.quietHoursStart = start;
        this.quietHoursEnd = end;
    }

    public void enableChannel(NotificationChannel channel) {
        this.enabledChannels.add(channel);
    }

    public void disableChannel(NotificationChannel channel) {
        this.enabledChannels.remove(channel);
    }
}

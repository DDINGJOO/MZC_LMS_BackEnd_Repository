package com.mzc.lms.notification.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationPreference {

    private Long id;
    private Long userId;

    @Builder.Default
    private Set<NotificationChannel> enabledChannels = new HashSet<>();

    private LocalTime quietHoursStart;
    private LocalTime quietHoursEnd;

    public boolean isChannelEnabled(NotificationChannel channel) {
        return enabledChannels.contains(channel);
    }

    public boolean isQuietHours(LocalTime time) {
        if (quietHoursStart == null || quietHoursEnd == null) {
            return false;
        }

        if (quietHoursStart.isBefore(quietHoursEnd)) {
            return !time.isBefore(quietHoursStart) && time.isBefore(quietHoursEnd);
        } else {
            // Quiet hours span midnight
            return !time.isBefore(quietHoursStart) || time.isBefore(quietHoursEnd);
        }
    }

    public void enableChannel(NotificationChannel channel) {
        this.enabledChannels.add(channel);
    }

    public void disableChannel(NotificationChannel channel) {
        this.enabledChannels.remove(channel);
    }

    public void updateQuietHours(LocalTime start, LocalTime end) {
        this.quietHoursStart = start;
        this.quietHoursEnd = end;
    }
}

package com.mzc.lms.profile.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdatedEvent {

    private Long userId;
    private String eventType;
    private String changedField;
    private String oldValue;
    private String newValue;
    private LocalDateTime occurredAt;

    public static ProfileUpdatedEvent nameChanged(Long userId, String oldName, String newName) {
        return ProfileUpdatedEvent.builder()
                .userId(userId)
                .eventType("PROFILE_NAME_CHANGED")
                .changedField("name")
                .oldValue(oldName)
                .newValue(newName)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static ProfileUpdatedEvent imageChanged(Long userId, String oldUrl, String newUrl) {
        return ProfileUpdatedEvent.builder()
                .userId(userId)
                .eventType("PROFILE_IMAGE_CHANGED")
                .changedField("profileImageUrl")
                .oldValue(oldUrl)
                .newValue(newUrl)
                .occurredAt(LocalDateTime.now())
                .build();
    }
}

package com.mzc.lms.identity.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자 인증 이벤트 (Kafka로 발행)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthenticatedEvent {

    private Long userId;
    private String email;
    private String userType;
    private String ipAddress;
    private String deviceInfo;
    private LocalDateTime authenticatedAt;

    public static UserAuthenticatedEvent of(Long userId, String email, String userType,
                                            String ipAddress, String deviceInfo) {
        return UserAuthenticatedEvent.builder()
                .userId(userId)
                .email(email)
                .userType(userType)
                .ipAddress(ipAddress)
                .deviceInfo(deviceInfo)
                .authenticatedAt(LocalDateTime.now())
                .build();
    }
}

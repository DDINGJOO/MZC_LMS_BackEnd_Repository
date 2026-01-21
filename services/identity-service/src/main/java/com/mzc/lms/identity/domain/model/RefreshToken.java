package com.mzc.lms.identity.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * RefreshToken Domain Model
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    private Long id;
    private Long userId;
    private String token;
    private String deviceInfo;
    private String ipAddress;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}

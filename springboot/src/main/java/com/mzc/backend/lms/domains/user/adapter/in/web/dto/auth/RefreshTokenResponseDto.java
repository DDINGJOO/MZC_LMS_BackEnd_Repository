package com.mzc.backend.lms.domains.user.adapter.in.web.dto.auth;

import lombok.Builder;
import lombok.Data;

/**
 * 토큰 갱신 응답 DTO
 */
@Data
@Builder
public class RefreshTokenResponseDto {

    private String accessToken;
    private String refreshToken;

    public static RefreshTokenResponseDto of(String accessToken, String refreshToken) {
        return RefreshTokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

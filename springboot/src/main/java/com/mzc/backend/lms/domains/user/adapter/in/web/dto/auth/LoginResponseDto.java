package com.mzc.backend.lms.domains.user.adapter.in.web.dto.auth;

import lombok.Builder;
import lombok.Data;

/**
 * 로그인 응답 DTO
 */
@Data
@Builder
public class LoginResponseDto {

    private String accessToken;
    private String refreshToken;
    private String userType;
    private String userNumber;
    private String name;
    private String email;
    private String userId;
    private String thumbnailUrl;
    private Long departmentId;
    private String departmentName;

    public static LoginResponseDto ofTokens(String accessToken, String refreshToken) {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static LoginResponseDto of(String accessToken, String refreshToken,
                                     String userType, String userNumber,
                                     String name, String email, String userId,
                                     String thumbnailUrl, Long departmentId, String departmentName) {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userType(userType)
                .userNumber(userNumber)
                .name(name)
                .email(email)
                .userId(userId)
                .thumbnailUrl(thumbnailUrl)
                .departmentId(departmentId)
                .departmentName(departmentName)
                .build();
    }
}

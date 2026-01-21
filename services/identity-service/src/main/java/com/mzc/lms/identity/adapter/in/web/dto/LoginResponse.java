package com.mzc.lms.identity.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private UserInfo user;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String email;
        private String name;
        private String userType;
        private Long userNumber;
    }

    public static LoginResponse of(String accessToken, String refreshToken, Long expiresIn,
                                   Long userId, String email, String name, String userType, Long userNumber) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .user(UserInfo.builder()
                        .id(userId)
                        .email(email)
                        .name(name)
                        .userType(userType)
                        .userNumber(userNumber)
                        .build())
                .build();
    }
}

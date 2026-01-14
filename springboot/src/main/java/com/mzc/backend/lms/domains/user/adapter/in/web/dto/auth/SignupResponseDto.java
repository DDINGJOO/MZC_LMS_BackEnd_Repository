package com.mzc.backend.lms.domains.user.adapter.in.web.dto.auth;

/**
 * 회원가입 응답 DTO
 */
public record SignupResponseDto(
        String userId,
        String message
) {
    /**
     * 정적 팩토리 메서드
     */
    public static SignupResponseDto of(String userId, String message) {
        return new SignupResponseDto(userId, message);
    }
}

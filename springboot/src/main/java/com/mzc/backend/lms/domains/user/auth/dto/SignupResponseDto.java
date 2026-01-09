package com.mzc.backend.lms.domains.user.auth.dto;

/**
 * 회원가입 응답 DTO
 */
public record SignupResponseDto(
        String userId,
        String message
) {}

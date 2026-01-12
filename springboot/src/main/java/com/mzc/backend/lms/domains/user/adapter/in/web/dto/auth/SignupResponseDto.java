package com.mzc.backend.lms.domains.user.adapter.in.web.dto.auth;

/**
 * 회원가입 응답 DTO
 */
public record SignupResponseDto(
        String userId,
        String message
) {}

package com.mzc.backend.lms.domains.user.adapter.in.web.dto.auth;

/**
 * 이메일 가용성 확인 응답 DTO
 */
public record EmailAvailabilityResponseDto(
        boolean available,
        String message
) {}

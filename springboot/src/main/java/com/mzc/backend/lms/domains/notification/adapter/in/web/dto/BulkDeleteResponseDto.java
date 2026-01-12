package com.mzc.backend.lms.domains.notification.adapter.in.web.dto;

/**
 * 일괄 삭제 응답 DTO
 */
public record BulkDeleteResponseDto(String message, int deletedCount) {}

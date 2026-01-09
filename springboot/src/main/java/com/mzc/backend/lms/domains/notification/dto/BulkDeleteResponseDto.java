package com.mzc.backend.lms.domains.notification.dto;

/**
 * 일괄 삭제 응답 DTO
 */
public record BulkDeleteResponseDto(String message, int deletedCount) {}

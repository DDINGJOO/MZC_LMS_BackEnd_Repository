package com.mzc.backend.lms.domains.notification.dto;

/**
 * 일괄 업데이트 응답 DTO
 */
public record BulkUpdateResponseDto(String message, int updatedCount) {}

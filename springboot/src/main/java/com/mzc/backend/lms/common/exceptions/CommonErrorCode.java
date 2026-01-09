package com.mzc.backend.lms.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 공통/시스템 에러 코드
 * <p>
 * 도메인에 종속되지 않는 공통 에러 코드를 정의합니다.
 * - 인증/인가 에러 (AUTH_XXX)
 * - 검증 에러 (VALIDATION_XXX)
 * - 시스템 에러 (SYSTEM_XXX)
 * </p>
 *
 * @see ErrorCode
 */
@Getter
public enum CommonErrorCode implements ErrorCode {

    // 인증/인가 관련 에러 (AUTH_0XX)
    UNAUTHORIZED("AUTH_001", "Unauthorized", HttpStatus.UNAUTHORIZED),

    // 검증 관련 에러 (VALIDATION_0XX)
    INVALID_INPUT("VALIDATION_001", "Invalid input", HttpStatus.BAD_REQUEST),
    REQUIRED_FIELD_MISSING("VALIDATION_002", "Required field is missing", HttpStatus.BAD_REQUEST),
    INVALID_FORMAT("VALIDATION_003", "Invalid format", HttpStatus.BAD_REQUEST),
    VALUE_OUT_OF_RANGE("VALIDATION_004", "Value is out of range", HttpStatus.BAD_REQUEST),

    // 시스템 에러 (SYSTEM_0XX)
    INTERNAL_SERVER_ERROR("SYSTEM_001", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_ERROR("SYSTEM_002", "Database error", HttpStatus.INTERNAL_SERVER_ERROR),
    EXTERNAL_API_ERROR("SYSTEM_003", "External API error", HttpStatus.BAD_GATEWAY),
    CACHE_ERROR("SYSTEM_004", "Cache error", HttpStatus.INTERNAL_SERVER_ERROR),
    EVENT_PUBLISH_FAILED("SYSTEM_005", "Failed to publish event", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    CommonErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("CommonErrorCode{code='%s', message='%s', status=%s}",
                code, message, status);
    }
}

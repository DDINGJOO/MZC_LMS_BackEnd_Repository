package com.mzc.backend.lms.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mzc.backend.lms.common.exceptions.ErrorCode;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * API 에러 정보 클래스
 * <p>
 * 에러 응답에 포함되는 상세 정보를 담습니다.
 * </p>
 *
 * @param code 에러 코드
 * @param message 에러 메시지
 * @param detail 상세 메시지 (선택)
 * @param fieldErrors 필드별 유효성 검증 에러 (선택)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorInfo(
        String code,
        String message,
        String detail,
        List<FieldErrorDetail> fieldErrors
) {

    /**
     * ErrorCode로부터 ErrorInfo 생성
     */
    public static ErrorInfo from(ErrorCode errorCode) {
        return new ErrorInfo(
                errorCode.getCode(),
                errorCode.getMessage(),
                null,
                null
        );
    }

    /**
     * ErrorCode + 상세 메시지로 ErrorInfo 생성
     */
    public static ErrorInfo from(ErrorCode errorCode, String detail) {
        return new ErrorInfo(
                errorCode.getCode(),
                errorCode.getMessage(),
                detail,
                null
        );
    }

    /**
     * ErrorCode + FieldError 목록으로 ErrorInfo 생성
     */
    public static ErrorInfo fromValidation(ErrorCode errorCode, List<FieldError> errors) {
        List<FieldErrorDetail> fieldErrorDetails = errors.stream()
                .map(error -> new FieldErrorDetail(
                        error.getField(),
                        error.getRejectedValue() != null ? error.getRejectedValue().toString() : null,
                        error.getDefaultMessage()
                ))
                .toList();

        return new ErrorInfo(
                errorCode.getCode(),
                errorCode.getMessage(),
                null,
                fieldErrorDetails
        );
    }

    /**
     * 코드, 메시지, Map 형태의 필드 에러로 ErrorInfo 생성
     */
    public static ErrorInfo of(String code, String message, Map<String, String> fieldErrors) {
        List<FieldErrorDetail> details = null;
        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            details = fieldErrors.entrySet().stream()
                    .map(entry -> new FieldErrorDetail(entry.getKey(), null, entry.getValue()))
                    .toList();
        }

        return new ErrorInfo(code, message, null, details);
    }

    /**
     * 필드 에러 상세 정보
     */
    public record FieldErrorDetail(
            String field,
            String rejectedValue,
            String message
    ) {}
}

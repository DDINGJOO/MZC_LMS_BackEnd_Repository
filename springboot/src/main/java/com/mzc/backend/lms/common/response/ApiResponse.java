package com.mzc.backend.lms.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mzc.backend.lms.common.exceptions.ErrorCode;

import java.time.LocalDateTime;

/**
 * API 응답 표준 래퍼 클래스
 * <p>
 * 모든 API 응답을 일관된 형식으로 래핑합니다.
 * </p>
 *
 * @param success 성공 여부
 * @param data 응답 데이터 (성공 시)
 * @param error 에러 정보 (실패 시)
 * @param timestamp 응답 시간
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        T data,
        ErrorInfo error,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp
) {

    /**
     * 성공 응답 생성 (데이터 포함)
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, LocalDateTime.now());
    }

    /**
     * 성공 응답 생성 (데이터 없음)
     */
    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(true, null, null, LocalDateTime.now());
    }

    /**
     * 성공 응답 생성 (메시지만 포함)
     */
    public static ApiResponse<MessageResponse> successMessage(String message) {
        return new ApiResponse<>(true, new MessageResponse(message), null, LocalDateTime.now());
    }

    /**
     * 에러 응답 생성 (ErrorCode 사용)
     */
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(false, null, ErrorInfo.from(errorCode), LocalDateTime.now());
    }

    /**
     * 에러 응답 생성 (ErrorCode + 상세 메시지)
     */
    public static <T> ApiResponse<T> error(ErrorCode errorCode, String detail) {
        return new ApiResponse<>(false, null, ErrorInfo.from(errorCode, detail), LocalDateTime.now());
    }

    /**
     * 에러 응답 생성 (ErrorInfo 직접 사용)
     */
    public static <T> ApiResponse<T> error(ErrorInfo errorInfo) {
        return new ApiResponse<>(false, null, errorInfo, LocalDateTime.now());
    }

    /**
     * 메시지 응답용 record
     */
    public record MessageResponse(String message) {}
}

package com.mzc.backend.lms.common.exceptions;

import org.springframework.http.HttpStatus;

/**
 * 에러 코드 표준 인터페이스 (Sealed Interface)
 * <p>
 * 모든 에러 코드 enum이 구현해야 하는 표준 계약을 정의합니다.
 * Sealed interface를 사용하여 허용된 구현체만 존재하도록 제한합니다.
 * </p>
 *
 * <h3>계층 구조:</h3>
 * <pre>
 * ErrorCode (sealed)
 * ├── CommonErrorCode (시스템/공통 에러)
 * └── DomainErrorCode (sealed) - 도메인별 에러
 *     ├── BoardErrorCode
 *     └── UserErrorCode
 * </pre>
 */
public sealed interface ErrorCode permits CommonErrorCode, DomainErrorCode {

    /**
     * 에러 코드 반환
     * 형식: PREFIX_XXX (예: AUTH_001, POST_001, USER_001)
     */
    String getCode();

    /**
     * 에러 메시지 반환
     */
    String getMessage();

    /**
     * HTTP 상태 코드 반환
     */
    HttpStatus getStatus();

    /**
     * HTTP 상태 코드 숫자 값 반환 (예: 400, 404, 500)
     */
    default int getStatusValue() {
        return getStatus().value();
    }

    /**
     * 클라이언트 에러 여부 (4xx)
     */
    default boolean isClientError() {
        return getStatus().is4xxClientError();
    }

    /**
     * 서버 에러 여부 (5xx)
     */
    default boolean isServerError() {
        return getStatus().is5xxServerError();
    }
}

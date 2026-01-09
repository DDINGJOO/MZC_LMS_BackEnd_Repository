package com.mzc.backend.lms.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 모든 예외의 기본 클래스
 * Domain Exception과 Application Exception의 공통 부모 클래스
 * ErrorCode 인터페이스를 통해 표준화된 에러 코드 제공
 */
@Getter
public abstract class CommonException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;

    protected CommonException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getStatus();
    }

    protected CommonException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getStatus();
    }

    protected CommonException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getStatus();
    }

    /**
     * 도메인별 ErrorCode의 HttpStatus를 오버라이드하는 생성자
     * 도메인 예외에서 자체 ErrorCode의 상태 코드를 사용할 때 활용
     */
    protected CommonException(ErrorCode errorCode, HttpStatus httpStatus, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    /**
     * 도메인별 ErrorCode의 HttpStatus를 오버라이드하는 생성자 (with cause)
     */
    protected CommonException(ErrorCode errorCode, HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    /**
     * 예외 타입 반환 (Domain/Application 구분용)
     * 예: "USER_DOMAIN", "APPLICATION"
     */
    public abstract String getExceptionType();
}

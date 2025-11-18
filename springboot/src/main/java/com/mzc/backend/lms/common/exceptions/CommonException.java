package com.mzc.backend.lms.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * PlaceInfo 도메인의 모든 예외의 기본 클래스
 * Domain Exception과 Application Exception의 공통 부모 클래스
 */
@Getter
public abstract class CommonException extends RuntimeException {

	private final CommonErrorCode commonErrorCode;
	private final HttpStatus httpStatus;

	protected CommonException(CommonErrorCode commonErrorCode) {
		super(commonErrorCode.getMessage());
		this.commonErrorCode = commonErrorCode;
		this.httpStatus = commonErrorCode.getStatus();
	}

	protected CommonException(CommonErrorCode commonErrorCode, String message) {
		super(message);
		this.commonErrorCode = commonErrorCode;
		this.httpStatus = commonErrorCode.getStatus();
	}

	protected CommonException(CommonErrorCode commonErrorCode, String message, Throwable cause) {
		super(message, cause);
		this.commonErrorCode = commonErrorCode;
		this.httpStatus = commonErrorCode.getStatus();
	}

	/**
	 * 예외 타입 반환 (Domain/Application 구분용)
	 */
	public abstract String getExceptionType();
}

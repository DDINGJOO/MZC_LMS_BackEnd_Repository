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
	 * 도메인별 ErrorCode의 HttpStatus를 사용하는 생성자
	 * 도메인 예외에서 자체 ErrorCode의 상태 코드를 사용할 때 활용
	 */
	protected CommonException(CommonErrorCode commonErrorCode, HttpStatus httpStatus, String message) {
		super(message);
		this.commonErrorCode = commonErrorCode;
		this.httpStatus = httpStatus;
	}

	/**
	 * 도메인별 ErrorCode의 HttpStatus를 사용하는 생성자 (with cause)
	 */
	protected CommonException(CommonErrorCode commonErrorCode, HttpStatus httpStatus, String message, Throwable cause) {
		super(message, cause);
		this.commonErrorCode = commonErrorCode;
		this.httpStatus = httpStatus;
	}

	/**
	 * 예외 타입 반환 (Domain/Application 구분용)
	 */
	public abstract String getExceptionType();
}

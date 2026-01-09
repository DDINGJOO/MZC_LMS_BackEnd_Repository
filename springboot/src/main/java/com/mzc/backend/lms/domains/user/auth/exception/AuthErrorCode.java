package com.mzc.backend.lms.domains.user.auth.exception;

import com.mzc.backend.lms.common.exceptions.DomainErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Auth 도메인 에러 코드
 * <p>
 * 인증/인가 관련 에러 코드를 정의합니다.
 * </p>
 */
@Getter
public enum AuthErrorCode implements DomainErrorCode {

    // 이메일 관련 (EMAIL_0XX)
    EMAIL_ALREADY_EXISTS("AUTH_EMAIL_001", "이미 가입된 이메일입니다", HttpStatus.CONFLICT),
    EMAIL_SEND_FAILED("AUTH_EMAIL_002", "인증 코드 발송에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_VERIFICATION_FAILED("AUTH_EMAIL_003", "인증 코드가 올바르지 않거나 만료되었습니다", HttpStatus.BAD_REQUEST),
    EMAIL_CHECK_FAILED("AUTH_EMAIL_004", "이메일 확인에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),

    // 회원가입 관련 (SIGNUP_0XX)
    SIGNUP_FAILED("AUTH_SIGNUP_001", "회원가입에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_SIGNUP_DATA("AUTH_SIGNUP_002", "잘못된 회원가입 정보입니다", HttpStatus.BAD_REQUEST),

    // 로그인 관련 (LOGIN_0XX)
    LOGIN_FAILED("AUTH_LOGIN_001", "로그인에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_CREDENTIALS("AUTH_LOGIN_002", "이메일 또는 비밀번호가 올바르지 않습니다", HttpStatus.BAD_REQUEST),
    ACCOUNT_LOCKED("AUTH_LOGIN_003", "계정이 잠겼습니다. 관리자에게 문의하세요", HttpStatus.FORBIDDEN),
    ACCOUNT_DISABLED("AUTH_LOGIN_004", "비활성화된 계정입니다", HttpStatus.FORBIDDEN),

    // 토큰 관련 (TOKEN_0XX)
    TOKEN_REFRESH_FAILED("AUTH_TOKEN_001", "토큰 갱신에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    TOKEN_INVALID("AUTH_TOKEN_002", "유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("AUTH_TOKEN_003", "만료된 토큰입니다", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID("AUTH_TOKEN_004", "유효하지 않은 리프레시 토큰입니다", HttpStatus.UNAUTHORIZED),

    // 암호화 관련 (ENCRYPTION_0XX)
    ENCRYPTION_FAILED("AUTH_ENCRYPT_001", "암호화에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    DECRYPTION_FAILED("AUTH_ENCRYPT_002", "복호화에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private static final String DOMAIN = "AUTH";

    private final String code;
    private final String message;
    private final HttpStatus status;

    AuthErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }
}

package com.mzc.backend.lms.domains.user.auth.exception;

import com.mzc.backend.lms.common.exceptions.CommonException;
import lombok.Getter;

/**
 * Auth 도메인 예외 클래스
 * <p>
 * 인증/인가 관련 예외를 처리합니다.
 * </p>
 */
@Getter
public class AuthException extends CommonException {

    private final AuthErrorCode authErrorCode;

    public AuthException(AuthErrorCode errorCode) {
        super(errorCode, errorCode.getStatus(), errorCode.getMessage());
        this.authErrorCode = errorCode;
    }

    public AuthException(AuthErrorCode errorCode, String detailMessage) {
        super(errorCode, errorCode.getStatus(),
              String.format("%s - %s", errorCode.getMessage(), detailMessage));
        this.authErrorCode = errorCode;
    }

    public AuthException(AuthErrorCode errorCode, Throwable cause) {
        super(errorCode, errorCode.getStatus(), errorCode.getMessage(), cause);
        this.authErrorCode = errorCode;
    }

    public AuthException(AuthErrorCode errorCode, String detailMessage, Throwable cause) {
        super(errorCode, errorCode.getStatus(),
              String.format("%s - %s", errorCode.getMessage(), detailMessage), cause);
        this.authErrorCode = errorCode;
    }

    @Override
    public String getExceptionType() {
        return "AUTH_DOMAIN";
    }

    // 팩토리 메서드들

    public static AuthException emailAlreadyExists(String email) {
        return new AuthException(AuthErrorCode.EMAIL_ALREADY_EXISTS,
            String.format("이메일: %s", email));
    }

    public static AuthException emailSendFailed(Throwable cause) {
        return new AuthException(AuthErrorCode.EMAIL_SEND_FAILED, cause);
    }

    public static AuthException emailVerificationFailed() {
        return new AuthException(AuthErrorCode.EMAIL_VERIFICATION_FAILED);
    }

    public static AuthException signupFailed(Throwable cause) {
        return new AuthException(AuthErrorCode.SIGNUP_FAILED, cause);
    }

    public static AuthException invalidSignupData(String reason) {
        return new AuthException(AuthErrorCode.INVALID_SIGNUP_DATA, reason);
    }

    public static AuthException loginFailed(Throwable cause) {
        return new AuthException(AuthErrorCode.LOGIN_FAILED, cause);
    }

    public static AuthException invalidCredentials() {
        return new AuthException(AuthErrorCode.INVALID_CREDENTIALS);
    }

    public static AuthException tokenRefreshFailed(Throwable cause) {
        return new AuthException(AuthErrorCode.TOKEN_REFRESH_FAILED, cause);
    }

    public static AuthException invalidToken() {
        return new AuthException(AuthErrorCode.TOKEN_INVALID);
    }

    public static AuthException tokenExpired() {
        return new AuthException(AuthErrorCode.TOKEN_EXPIRED);
    }

    public static AuthException invalidRefreshToken() {
        return new AuthException(AuthErrorCode.REFRESH_TOKEN_INVALID);
    }

    public static AuthException emailCheckFailed(Throwable cause) {
        return new AuthException(AuthErrorCode.EMAIL_CHECK_FAILED, cause);
    }

    public static AuthException encryptionFailed(Throwable cause) {
        return new AuthException(AuthErrorCode.ENCRYPTION_FAILED, cause);
    }

    public static AuthException decryptionFailed(Throwable cause) {
        return new AuthException(AuthErrorCode.DECRYPTION_FAILED, cause);
    }

    public static AuthException unauthorized() {
        return new AuthException(AuthErrorCode.UNAUTHORIZED);
    }

    public static AuthException accessDenied() {
        return new AuthException(AuthErrorCode.ACCESS_DENIED);
    }
}

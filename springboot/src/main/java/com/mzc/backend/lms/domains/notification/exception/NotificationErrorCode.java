package com.mzc.backend.lms.domains.notification.exception;

import com.mzc.backend.lms.common.exceptions.DomainErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Notification 도메인 에러 코드
 * <p>
 * 알림 관련 에러 코드를 정의합니다.
 * </p>
 */
@Getter
public enum NotificationErrorCode implements DomainErrorCode {

    // 알림 관련 (NOTIFICATION_0XX)
    NOTIFICATION_NOT_FOUND("NOTIFICATION_001", "알림을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    NOT_NOTIFICATION_OWNER("NOTIFICATION_002", "해당 알림에 대한 권한이 없습니다", HttpStatus.FORBIDDEN),
    ALREADY_READ("NOTIFICATION_003", "이미 읽은 알림입니다", HttpStatus.BAD_REQUEST),

    // 알림 설정 관련 (SETTING_0XX)
    SETTING_NOT_FOUND("NOTIFICATION_SETTING_001", "알림 설정을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    INVALID_NOTIFICATION_TYPE("NOTIFICATION_SETTING_002", "유효하지 않은 알림 타입입니다", HttpStatus.BAD_REQUEST),

    // 전송 관련 (SEND_0XX)
    SEND_FAILED("NOTIFICATION_SEND_001", "알림 전송에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    RECIPIENT_NOT_FOUND("NOTIFICATION_SEND_002", "수신자를 찾을 수 없습니다", HttpStatus.BAD_REQUEST),
    ;

    private static final String DOMAIN = "NOTIFICATION";

    private final String code;
    private final String message;
    private final HttpStatus status;

    NotificationErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }
}

package com.mzc.backend.lms.domains.notification.exception;

import com.mzc.backend.lms.common.exceptions.CommonException;
import lombok.Getter;

/**
 * Notification 도메인 예외 클래스
 * <p>
 * 알림 관련 예외를 처리합니다.
 * </p>
 */
@Getter
public class NotificationException extends CommonException {

    private final NotificationErrorCode notificationErrorCode;

    public NotificationException(NotificationErrorCode errorCode) {
        super(errorCode, errorCode.getStatus(), errorCode.getMessage());
        this.notificationErrorCode = errorCode;
    }

    public NotificationException(NotificationErrorCode errorCode, String detailMessage) {
        super(errorCode, errorCode.getStatus(),
              String.format("%s - %s", errorCode.getMessage(), detailMessage));
        this.notificationErrorCode = errorCode;
    }

    public NotificationException(NotificationErrorCode errorCode, Throwable cause) {
        super(errorCode, errorCode.getStatus(), errorCode.getMessage(), cause);
        this.notificationErrorCode = errorCode;
    }

    public NotificationException(NotificationErrorCode errorCode, String detailMessage, Throwable cause) {
        super(errorCode, errorCode.getStatus(),
              String.format("%s - %s", errorCode.getMessage(), detailMessage), cause);
        this.notificationErrorCode = errorCode;
    }

    @Override
    public String getExceptionType() {
        return "NOTIFICATION_DOMAIN";
    }

    // 팩토리 메서드들 - 알림 관련

    public static NotificationException notificationNotFound(Long notificationId) {
        return new NotificationException(NotificationErrorCode.NOTIFICATION_NOT_FOUND,
            String.format("알림 ID: %d", notificationId));
    }

    public static NotificationException notNotificationOwner(Long userId, Long notificationId) {
        return new NotificationException(NotificationErrorCode.NOT_NOTIFICATION_OWNER,
            String.format("사용자 ID: %d, 알림 ID: %d", userId, notificationId));
    }

    public static NotificationException alreadyRead(Long notificationId) {
        return new NotificationException(NotificationErrorCode.ALREADY_READ,
            String.format("알림 ID: %d", notificationId));
    }

    // 팩토리 메서드들 - 알림 설정 관련

    public static NotificationException settingNotFound(Long userId) {
        return new NotificationException(NotificationErrorCode.SETTING_NOT_FOUND,
            String.format("사용자 ID: %d", userId));
    }

    public static NotificationException invalidNotificationType(String type) {
        return new NotificationException(NotificationErrorCode.INVALID_NOTIFICATION_TYPE,
            String.format("타입: %s", type));
    }

    // 팩토리 메서드들 - 전송 관련

    public static NotificationException sendFailed(Throwable cause) {
        return new NotificationException(NotificationErrorCode.SEND_FAILED, cause);
    }

    public static NotificationException sendFailed(String reason) {
        return new NotificationException(NotificationErrorCode.SEND_FAILED, reason);
    }

    public static NotificationException recipientNotFound(Long recipientId) {
        return new NotificationException(NotificationErrorCode.RECIPIENT_NOT_FOUND,
            String.format("수신자 ID: %d", recipientId));
    }
}

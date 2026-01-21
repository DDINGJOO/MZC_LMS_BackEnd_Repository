package com.mzc.backend.lms.domains.notification.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("NotificationException Tests")
class NotificationExceptionTest {

    @Nested
    @DisplayName("Factory Methods - Notification Related")
    class NotificationFactoryMethodTests {

        @Test
        @DisplayName("Given notificationId When notificationNotFound Then creates exception with correct code and message")
        void notificationNotFound_shouldCreateExceptionWithCorrectDetails() {
            // Given
            Long notificationId = 123L;

            // When
            NotificationException exception = NotificationException.notificationNotFound(notificationId);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getNotificationErrorCode()).isEqualTo(NotificationErrorCode.NOTIFICATION_NOT_FOUND);
            assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(exception.getMessage()).contains("알림을 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("알림 ID: 123");
            assertThat(exception.getExceptionType()).isEqualTo("NOTIFICATION_DOMAIN");
        }

        @Test
        @DisplayName("Given userId and notificationId When notNotificationOwner Then creates exception with correct details")
        void notNotificationOwner_shouldCreateExceptionWithCorrectDetails() {
            // Given
            Long userId = 1L;
            Long notificationId = 123L;

            // When
            NotificationException exception = NotificationException.notNotificationOwner(userId, notificationId);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getNotificationErrorCode()).isEqualTo(NotificationErrorCode.NOT_NOTIFICATION_OWNER);
            assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(exception.getMessage()).contains("해당 알림에 대한 권한이 없습니다");
            assertThat(exception.getMessage()).contains("사용자 ID: 1");
            assertThat(exception.getMessage()).contains("알림 ID: 123");
        }

        @Test
        @DisplayName("Given notificationId When alreadyRead Then creates exception with correct details")
        void alreadyRead_shouldCreateExceptionWithCorrectDetails() {
            // Given
            Long notificationId = 123L;

            // When
            NotificationException exception = NotificationException.alreadyRead(notificationId);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getNotificationErrorCode()).isEqualTo(NotificationErrorCode.ALREADY_READ);
            assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(exception.getMessage()).contains("이미 읽은 알림입니다");
            assertThat(exception.getMessage()).contains("알림 ID: 123");
        }
    }

    @Nested
    @DisplayName("Factory Methods - Setting Related")
    class SettingFactoryMethodTests {

        @Test
        @DisplayName("Given userId When settingNotFound Then creates exception with correct details")
        void settingNotFound_shouldCreateExceptionWithCorrectDetails() {
            // Given
            Long userId = 1L;

            // When
            NotificationException exception = NotificationException.settingNotFound(userId);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getNotificationErrorCode()).isEqualTo(NotificationErrorCode.SETTING_NOT_FOUND);
            assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(exception.getMessage()).contains("알림 설정을 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("사용자 ID: 1");
        }

        @Test
        @DisplayName("Given invalid type When invalidNotificationType Then creates exception with correct details")
        void invalidNotificationType_shouldCreateExceptionWithCorrectDetails() {
            // Given
            String type = "INVALID_TYPE";

            // When
            NotificationException exception = NotificationException.invalidNotificationType(type);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getNotificationErrorCode()).isEqualTo(NotificationErrorCode.INVALID_NOTIFICATION_TYPE);
            assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(exception.getMessage()).contains("유효하지 않은 알림 타입입니다");
            assertThat(exception.getMessage()).contains("타입: INVALID_TYPE");
        }
    }

    @Nested
    @DisplayName("Factory Methods - Send Related")
    class SendFactoryMethodTests {

        @Test
        @DisplayName("Given cause When sendFailed Then creates exception with correct details")
        void sendFailed_withCause_shouldCreateExceptionWithCorrectDetails() {
            // Given
            Throwable cause = new RuntimeException("Network error");

            // When
            NotificationException exception = NotificationException.sendFailed(cause);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getNotificationErrorCode()).isEqualTo(NotificationErrorCode.SEND_FAILED);
            assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(exception.getMessage()).contains("알림 전송에 실패했습니다");
            assertThat(exception.getCause()).isEqualTo(cause);
        }

        @Test
        @DisplayName("Given reason When sendFailed Then creates exception with correct details")
        void sendFailed_withReason_shouldCreateExceptionWithCorrectDetails() {
            // Given
            String reason = "Invalid recipient email";

            // When
            NotificationException exception = NotificationException.sendFailed(reason);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getNotificationErrorCode()).isEqualTo(NotificationErrorCode.SEND_FAILED);
            assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(exception.getMessage()).contains("알림 전송에 실패했습니다");
            assertThat(exception.getMessage()).contains("Invalid recipient email");
        }

        @Test
        @DisplayName("Given recipientId When recipientNotFound Then creates exception with correct details")
        void recipientNotFound_shouldCreateExceptionWithCorrectDetails() {
            // Given
            Long recipientId = 123L;

            // When
            NotificationException exception = NotificationException.recipientNotFound(recipientId);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getNotificationErrorCode()).isEqualTo(NotificationErrorCode.RECIPIENT_NOT_FOUND);
            assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(exception.getMessage()).contains("수신자를 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("수신자 ID: 123");
        }
    }

    @Nested
    @DisplayName("Error Code Tests")
    class ErrorCodeTests {

        @Test
        @DisplayName("NOTIFICATION_NOT_FOUND should have correct code and status")
        void notificationNotFound_shouldHaveCorrectCodeAndStatus() {
            // Given & When
            NotificationErrorCode errorCode = NotificationErrorCode.NOTIFICATION_NOT_FOUND;

            // Then
            assertThat(errorCode.getCode()).isEqualTo("NOTIFICATION_001");
            assertThat(errorCode.getMessage()).isEqualTo("알림을 찾을 수 없습니다");
            assertThat(errorCode.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(errorCode.getDomain()).isEqualTo("NOTIFICATION");
        }

        @Test
        @DisplayName("NOT_NOTIFICATION_OWNER should have correct code and status")
        void notNotificationOwner_shouldHaveCorrectCodeAndStatus() {
            // Given & When
            NotificationErrorCode errorCode = NotificationErrorCode.NOT_NOTIFICATION_OWNER;

            // Then
            assertThat(errorCode.getCode()).isEqualTo("NOTIFICATION_002");
            assertThat(errorCode.getMessage()).isEqualTo("해당 알림에 대한 권한이 없습니다");
            assertThat(errorCode.getStatus()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(errorCode.getDomain()).isEqualTo("NOTIFICATION");
        }

        @Test
        @DisplayName("ALREADY_READ should have correct code and status")
        void alreadyRead_shouldHaveCorrectCodeAndStatus() {
            // Given & When
            NotificationErrorCode errorCode = NotificationErrorCode.ALREADY_READ;

            // Then
            assertThat(errorCode.getCode()).isEqualTo("NOTIFICATION_003");
            assertThat(errorCode.getMessage()).isEqualTo("이미 읽은 알림입니다");
            assertThat(errorCode.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("SETTING_NOT_FOUND should have correct code and status")
        void settingNotFound_shouldHaveCorrectCodeAndStatus() {
            // Given & When
            NotificationErrorCode errorCode = NotificationErrorCode.SETTING_NOT_FOUND;

            // Then
            assertThat(errorCode.getCode()).isEqualTo("NOTIFICATION_SETTING_001");
            assertThat(errorCode.getMessage()).isEqualTo("알림 설정을 찾을 수 없습니다");
            assertThat(errorCode.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("INVALID_NOTIFICATION_TYPE should have correct code and status")
        void invalidNotificationType_shouldHaveCorrectCodeAndStatus() {
            // Given & When
            NotificationErrorCode errorCode = NotificationErrorCode.INVALID_NOTIFICATION_TYPE;

            // Then
            assertThat(errorCode.getCode()).isEqualTo("NOTIFICATION_SETTING_002");
            assertThat(errorCode.getMessage()).isEqualTo("유효하지 않은 알림 타입입니다");
            assertThat(errorCode.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("SEND_FAILED should have correct code and status")
        void sendFailed_shouldHaveCorrectCodeAndStatus() {
            // Given & When
            NotificationErrorCode errorCode = NotificationErrorCode.SEND_FAILED;

            // Then
            assertThat(errorCode.getCode()).isEqualTo("NOTIFICATION_SEND_001");
            assertThat(errorCode.getMessage()).isEqualTo("알림 전송에 실패했습니다");
            assertThat(errorCode.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        @DisplayName("RECIPIENT_NOT_FOUND should have correct code and status")
        void recipientNotFound_shouldHaveCorrectCodeAndStatus() {
            // Given & When
            NotificationErrorCode errorCode = NotificationErrorCode.RECIPIENT_NOT_FOUND;

            // Then
            assertThat(errorCode.getCode()).isEqualTo("NOTIFICATION_SEND_002");
            assertThat(errorCode.getMessage()).isEqualTo("수신자를 찾을 수 없습니다");
            assertThat(errorCode.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("All error codes should have NOTIFICATION domain")
        void allErrorCodes_shouldHaveNotificationDomain() {
            // Given & When & Then
            for (NotificationErrorCode errorCode : NotificationErrorCode.values()) {
                assertThat(errorCode.getDomain()).isEqualTo("NOTIFICATION");
            }
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Given errorCode When create exception Then sets fields correctly")
        void constructor_withErrorCode_shouldSetFieldsCorrectly() {
            // Given
            NotificationErrorCode errorCode = NotificationErrorCode.NOTIFICATION_NOT_FOUND;

            // When
            NotificationException exception = new NotificationException(errorCode);

            // Then
            assertThat(exception.getNotificationErrorCode()).isEqualTo(errorCode);
            assertThat(exception.getHttpStatus()).isEqualTo(errorCode.getStatus());
            assertThat(exception.getMessage()).isEqualTo(errorCode.getMessage());
            assertThat(exception.getExceptionType()).isEqualTo("NOTIFICATION_DOMAIN");
        }

        @Test
        @DisplayName("Given errorCode and detail When create exception Then appends detail to message")
        void constructor_withErrorCodeAndDetail_shouldAppendDetail() {
            // Given
            NotificationErrorCode errorCode = NotificationErrorCode.NOTIFICATION_NOT_FOUND;
            String detail = "Additional context";

            // When
            NotificationException exception = new NotificationException(errorCode, detail);

            // Then
            assertThat(exception.getNotificationErrorCode()).isEqualTo(errorCode);
            assertThat(exception.getMessage()).contains(errorCode.getMessage());
            assertThat(exception.getMessage()).contains(detail);
        }

        @Test
        @DisplayName("Given errorCode and cause When create exception Then sets cause")
        void constructor_withErrorCodeAndCause_shouldSetCause() {
            // Given
            NotificationErrorCode errorCode = NotificationErrorCode.SEND_FAILED;
            Throwable cause = new RuntimeException("Root cause");

            // When
            NotificationException exception = new NotificationException(errorCode, cause);

            // Then
            assertThat(exception.getNotificationErrorCode()).isEqualTo(errorCode);
            assertThat(exception.getCause()).isEqualTo(cause);
        }

        @Test
        @DisplayName("Given errorCode, detail, and cause When create exception Then sets all fields")
        void constructor_withAllParameters_shouldSetAllFields() {
            // Given
            NotificationErrorCode errorCode = NotificationErrorCode.SEND_FAILED;
            String detail = "Network timeout";
            Throwable cause = new RuntimeException("Connection refused");

            // When
            NotificationException exception = new NotificationException(errorCode, detail, cause);

            // Then
            assertThat(exception.getNotificationErrorCode()).isEqualTo(errorCode);
            assertThat(exception.getMessage()).contains(errorCode.getMessage());
            assertThat(exception.getMessage()).contains(detail);
            assertThat(exception.getCause()).isEqualTo(cause);
        }
    }
}

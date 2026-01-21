package com.mzc.backend.lms.domains.notification.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DeadLetterMessage 테스트")
class DeadLetterMessageTest {

    @Nested
    @DisplayName("팩토리 메소드 테스트")
    class FactoryMethodTests {

        @Test
        @DisplayName("단일 알림 메시지로 DLQ 메시지 생성")
        void fromNotification_shouldCreateDlqMessage() {
            // given
            NotificationMessage message = NotificationMessage.builder()
                    .typeId(1)
                    .recipientId(100L)
                    .senderId(1L)
                    .title("테스트 알림")
                    .message("테스트 내용")
                    .build();
            String failureReason = "Connection timeout";
            int retryCount = 3;

            // when
            DeadLetterMessage dlqMessage = DeadLetterMessage.fromNotification(message, failureReason, retryCount);

            // then
            assertThat(dlqMessage).isNotNull();
            assertThat(dlqMessage.getMessageId()).startsWith("dlq-");
            assertThat(dlqMessage.getMessageType()).isEqualTo(DeadLetterMessage.MessageType.SINGLE);
            assertThat(dlqMessage.getNotificationMessage()).isEqualTo(message);
            assertThat(dlqMessage.getBatchNotificationMessage()).isNull();
            assertThat(dlqMessage.getFailureReason()).isEqualTo(failureReason);
            assertThat(dlqMessage.getRetryCount()).isEqualTo(retryCount);
            assertThat(dlqMessage.getFailedAt()).isNotNull();
            assertThat(dlqMessage.isSingleMessage()).isTrue();
            assertThat(dlqMessage.isBatchMessage()).isFalse();
        }

        @Test
        @DisplayName("배치 알림 메시지로 DLQ 메시지 생성")
        void fromBatchNotification_shouldCreateDlqMessage() {
            // given
            BatchNotificationMessage message = BatchNotificationMessage.builder()
                    .batchId(123L)
                    .typeId(2)
                    .recipientIds(List.of(1L, 2L, 3L))
                    .title("배치 알림")
                    .message("배치 내용")
                    .build();
            String failureReason = "Database error";
            int retryCount = 3;

            // when
            DeadLetterMessage dlqMessage = DeadLetterMessage.fromBatchNotification(message, failureReason, retryCount);

            // then
            assertThat(dlqMessage).isNotNull();
            assertThat(dlqMessage.getMessageId()).startsWith("dlq-");
            assertThat(dlqMessage.getMessageType()).isEqualTo(DeadLetterMessage.MessageType.BATCH);
            assertThat(dlqMessage.getNotificationMessage()).isNull();
            assertThat(dlqMessage.getBatchNotificationMessage()).isEqualTo(message);
            assertThat(dlqMessage.getFailureReason()).isEqualTo(failureReason);
            assertThat(dlqMessage.getRetryCount()).isEqualTo(retryCount);
            assertThat(dlqMessage.isSingleMessage()).isFalse();
            assertThat(dlqMessage.isBatchMessage()).isTrue();
        }
    }

    @Nested
    @DisplayName("재시도 테스트")
    class RetryTests {

        @Test
        @DisplayName("재시도 시 retryCount 증가 및 lastRetryAt 설정")
        void withRetry_shouldIncrementRetryCountAndSetLastRetryAt() {
            // given
            NotificationMessage message = NotificationMessage.builder()
                    .typeId(1)
                    .recipientId(100L)
                    .title("테스트")
                    .build();
            DeadLetterMessage original = DeadLetterMessage.fromNotification(message, "error", 1);

            // when
            DeadLetterMessage retried = original.withRetry();

            // then
            assertThat(retried.getRetryCount()).isEqualTo(2);
            assertThat(retried.getLastRetryAt()).isNotNull();
            assertThat(retried.getMessageId()).isEqualTo(original.getMessageId());
            assertThat(retried.getMessageType()).isEqualTo(original.getMessageType());
            assertThat(retried.getFailureReason()).isEqualTo(original.getFailureReason());
            assertThat(retried.getFailedAt()).isEqualTo(original.getFailedAt());
        }

        @Test
        @DisplayName("여러 번 재시도 시 retryCount 누적")
        void multipleRetries_shouldAccumulateRetryCount() {
            // given
            NotificationMessage message = NotificationMessage.builder()
                    .typeId(1)
                    .recipientId(100L)
                    .build();
            DeadLetterMessage dlqMessage = DeadLetterMessage.fromNotification(message, "error", 0);

            // when
            DeadLetterMessage retry1 = dlqMessage.withRetry();
            DeadLetterMessage retry2 = retry1.withRetry();
            DeadLetterMessage retry3 = retry2.withRetry();

            // then
            assertThat(retry1.getRetryCount()).isEqualTo(1);
            assertThat(retry2.getRetryCount()).isEqualTo(2);
            assertThat(retry3.getRetryCount()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("메시지 타입 확인 테스트")
    class MessageTypeTests {

        @Test
        @DisplayName("단일 메시지 타입 확인")
        void isSingleMessage_shouldReturnTrueForSingleType() {
            // given
            NotificationMessage message = NotificationMessage.builder()
                    .typeId(1)
                    .recipientId(100L)
                    .build();

            // when
            DeadLetterMessage dlqMessage = DeadLetterMessage.fromNotification(message, "error", 1);

            // then
            assertThat(dlqMessage.isSingleMessage()).isTrue();
            assertThat(dlqMessage.isBatchMessage()).isFalse();
        }

        @Test
        @DisplayName("배치 메시지 타입 확인")
        void isBatchMessage_shouldReturnTrueForBatchType() {
            // given
            BatchNotificationMessage message = BatchNotificationMessage.builder()
                    .batchId(123L)
                    .typeId(1)
                    .recipientIds(List.of(1L))
                    .build();

            // when
            DeadLetterMessage dlqMessage = DeadLetterMessage.fromBatchNotification(message, "error", 1);

            // then
            assertThat(dlqMessage.isSingleMessage()).isFalse();
            assertThat(dlqMessage.isBatchMessage()).isTrue();
        }
    }

    @Nested
    @DisplayName("메시지 ID 생성 테스트")
    class MessageIdTests {

        @Test
        @DisplayName("메시지 ID는 dlq- 접두사로 시작")
        void messageId_shouldStartWithDlqPrefix() {
            // given
            NotificationMessage message = NotificationMessage.builder()
                    .typeId(1)
                    .recipientId(100L)
                    .build();

            // when
            DeadLetterMessage dlqMessage = DeadLetterMessage.fromNotification(message, "error", 1);

            // then
            assertThat(dlqMessage.getMessageId()).startsWith("dlq-");
        }

        @Test
        @DisplayName("각 메시지는 고유한 ID를 가짐")
        void messageId_shouldBeUnique() {
            // given
            NotificationMessage message = NotificationMessage.builder()
                    .typeId(1)
                    .recipientId(100L)
                    .build();

            // when
            DeadLetterMessage dlq1 = DeadLetterMessage.fromNotification(message, "error", 1);
            DeadLetterMessage dlq2 = DeadLetterMessage.fromNotification(message, "error", 1);

            // then
            assertThat(dlq1.getMessageId()).isNotEqualTo(dlq2.getMessageId());
        }
    }

    @Nested
    @DisplayName("타임스탬프 테스트")
    class TimestampTests {

        @Test
        @DisplayName("생성 시 failedAt 자동 설정")
        void failedAt_shouldBeSetOnCreation() {
            // given
            LocalDateTime before = LocalDateTime.now();
            NotificationMessage message = NotificationMessage.builder()
                    .typeId(1)
                    .recipientId(100L)
                    .build();

            // when
            DeadLetterMessage dlqMessage = DeadLetterMessage.fromNotification(message, "error", 1);
            LocalDateTime after = LocalDateTime.now();

            // then
            assertThat(dlqMessage.getFailedAt()).isNotNull();
            assertThat(dlqMessage.getFailedAt()).isAfterOrEqualTo(before);
            assertThat(dlqMessage.getFailedAt()).isBeforeOrEqualTo(after);
        }

        @Test
        @DisplayName("생성 시 lastRetryAt은 null")
        void lastRetryAt_shouldBeNullOnCreation() {
            // given
            NotificationMessage message = NotificationMessage.builder()
                    .typeId(1)
                    .recipientId(100L)
                    .build();

            // when
            DeadLetterMessage dlqMessage = DeadLetterMessage.fromNotification(message, "error", 1);

            // then
            assertThat(dlqMessage.getLastRetryAt()).isNull();
        }
    }
}

package com.mzc.backend.lms.domains.notification.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Dead Letter Queue 메시지
 * 처리 실패한 알림 메시지를 래핑하여 실패 정보와 함께 저장
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeadLetterMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * DLQ 메시지 고유 ID
     */
    private String messageId;

    /**
     * 메시지 타입 (SINGLE / BATCH)
     */
    private MessageType messageType;

    /**
     * 원본 단일 알림 메시지 (messageType == SINGLE일 때)
     */
    private NotificationMessage notificationMessage;

    /**
     * 원본 배치 알림 메시지 (messageType == BATCH일 때)
     */
    private BatchNotificationMessage batchNotificationMessage;

    /**
     * 실패 사유
     */
    private String failureReason;

    /**
     * 실패 발생 시각
     */
    private LocalDateTime failedAt;

    /**
     * 재시도 횟수
     */
    private int retryCount;

    /**
     * 마지막 재시도 시각
     */
    private LocalDateTime lastRetryAt;

    public enum MessageType {
        SINGLE, BATCH
    }

    /**
     * 단일 알림 메시지로 DLQ 메시지 생성
     */
    public static DeadLetterMessage fromNotification(NotificationMessage message, String failureReason, int retryCount) {
        return DeadLetterMessage.builder()
                .messageId(generateMessageId())
                .messageType(MessageType.SINGLE)
                .notificationMessage(message)
                .failureReason(failureReason)
                .failedAt(LocalDateTime.now())
                .retryCount(retryCount)
                .build();
    }

    /**
     * 배치 알림 메시지로 DLQ 메시지 생성
     */
    public static DeadLetterMessage fromBatchNotification(BatchNotificationMessage message, String failureReason, int retryCount) {
        return DeadLetterMessage.builder()
                .messageId(generateMessageId())
                .messageType(MessageType.BATCH)
                .batchNotificationMessage(message)
                .failureReason(failureReason)
                .failedAt(LocalDateTime.now())
                .retryCount(retryCount)
                .build();
    }

    /**
     * 재시도 시 업데이트
     */
    public DeadLetterMessage withRetry() {
        return DeadLetterMessage.builder()
                .messageId(this.messageId)
                .messageType(this.messageType)
                .notificationMessage(this.notificationMessage)
                .batchNotificationMessage(this.batchNotificationMessage)
                .failureReason(this.failureReason)
                .failedAt(this.failedAt)
                .retryCount(this.retryCount + 1)
                .lastRetryAt(LocalDateTime.now())
                .build();
    }

    private static String generateMessageId() {
        return "dlq-" + System.currentTimeMillis() + "-" + java.util.UUID.randomUUID().toString().substring(0, 8);
    }

    public boolean isSingleMessage() {
        return MessageType.SINGLE.equals(this.messageType);
    }

    public boolean isBatchMessage() {
        return MessageType.BATCH.equals(this.messageType);
    }
}

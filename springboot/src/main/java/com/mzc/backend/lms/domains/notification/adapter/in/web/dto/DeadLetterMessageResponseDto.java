package com.mzc.backend.lms.domains.notification.adapter.in.web.dto;

import com.mzc.backend.lms.domains.notification.domain.model.DeadLetterMessage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DeadLetterMessageResponseDto {

    private String messageId;
    private String messageType;
    private String failureReason;
    private LocalDateTime failedAt;
    private int retryCount;
    private LocalDateTime lastRetryAt;

    // 단일 알림 정보
    private Long recipientId;
    private String title;
    private String message;

    // 배치 알림 정보
    private Long batchId;
    private Integer recipientCount;

    public static DeadLetterMessageResponseDto from(DeadLetterMessage dlqMessage) {
        DeadLetterMessageResponseDtoBuilder builder = DeadLetterMessageResponseDto.builder()
                .messageId(dlqMessage.getMessageId())
                .messageType(dlqMessage.getMessageType().name())
                .failureReason(dlqMessage.getFailureReason())
                .failedAt(dlqMessage.getFailedAt())
                .retryCount(dlqMessage.getRetryCount())
                .lastRetryAt(dlqMessage.getLastRetryAt());

        if (dlqMessage.isSingleMessage() && dlqMessage.getNotificationMessage() != null) {
            builder.recipientId(dlqMessage.getNotificationMessage().getRecipientId())
                    .title(dlqMessage.getNotificationMessage().getTitle())
                    .message(dlqMessage.getNotificationMessage().getMessage());
        } else if (dlqMessage.isBatchMessage() && dlqMessage.getBatchNotificationMessage() != null) {
            builder.batchId(dlqMessage.getBatchNotificationMessage().getBatchId())
                    .recipientCount(dlqMessage.getBatchNotificationMessage().getRecipientCount());
        }

        return builder.build();
    }
}

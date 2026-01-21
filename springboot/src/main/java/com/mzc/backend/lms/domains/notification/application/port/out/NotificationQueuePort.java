package com.mzc.backend.lms.domains.notification.application.port.out;

import com.mzc.backend.lms.domains.notification.domain.model.BatchNotificationMessage;
import com.mzc.backend.lms.domains.notification.domain.model.DeadLetterMessage;
import com.mzc.backend.lms.domains.notification.domain.model.NotificationMessage;

import java.util.List;
import java.util.Optional;

/**
 * 알림 큐 Port
 */
public interface NotificationQueuePort {

    /**
     * 단일 알림 메시지를 큐에 발행
     */
    void publish(NotificationMessage message);

    /**
     * 배치 알림 메시지를 큐에 발행
     */
    void publishBatch(BatchNotificationMessage message);

    /**
     * 큐에서 알림 메시지를 가져옴 (blocking)
     */
    Optional<NotificationMessage> dequeue(long timeoutSeconds);

    /**
     * 큐에서 배치 알림 메시지를 가져옴 (blocking)
     */
    Optional<BatchNotificationMessage> dequeueBatch(long timeoutSeconds);

    // ===== Dead Letter Queue 메소드 =====

    /**
     * 실패한 단일 알림 메시지를 Dead Letter Queue에 저장
     */
    void publishToDeadLetterQueue(NotificationMessage message, String failureReason, int retryCount);

    /**
     * 실패한 배치 알림 메시지를 Dead Letter Queue에 저장
     */
    void publishBatchToDeadLetterQueue(BatchNotificationMessage message, String failureReason, int retryCount);

    /**
     * Dead Letter Queue에서 메시지 목록 조회
     */
    List<DeadLetterMessage> getDeadLetterMessages(int offset, int limit);

    /**
     * Dead Letter Queue의 메시지 수 조회
     */
    long getDeadLetterQueueSize();

    /**
     * Dead Letter Queue에서 특정 메시지 조회
     */
    Optional<DeadLetterMessage> getDeadLetterMessage(String messageId);

    /**
     * Dead Letter Queue에서 메시지 삭제
     */
    boolean removeFromDeadLetterQueue(String messageId);

    /**
     * Dead Letter Queue 메시지를 원본 큐로 재발행
     */
    boolean reprocessDeadLetterMessage(String messageId);
}

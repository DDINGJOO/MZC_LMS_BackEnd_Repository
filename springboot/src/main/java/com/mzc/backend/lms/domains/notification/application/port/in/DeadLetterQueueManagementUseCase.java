package com.mzc.backend.lms.domains.notification.application.port.in;

import com.mzc.backend.lms.domains.notification.domain.model.DeadLetterMessage;

import java.util.List;
import java.util.Optional;

/**
 * Dead Letter Queue 관리 UseCase
 */
public interface DeadLetterQueueManagementUseCase {

    /**
     * DLQ 메시지 목록 조회
     */
    List<DeadLetterMessage> getDeadLetterMessages(int offset, int limit);

    /**
     * DLQ 메시지 수 조회
     */
    long getDeadLetterQueueSize();

    /**
     * 특정 DLQ 메시지 조회
     */
    Optional<DeadLetterMessage> getDeadLetterMessage(String messageId);

    /**
     * DLQ 메시지 삭제
     */
    boolean removeDeadLetterMessage(String messageId);

    /**
     * DLQ 메시지 재처리
     */
    boolean reprocessDeadLetterMessage(String messageId);

    /**
     * 모든 DLQ 메시지 재처리
     * @return [성공 수, 실패 수]
     */
    int[] reprocessAllDeadLetterMessages();
}

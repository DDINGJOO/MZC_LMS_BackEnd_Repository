package com.mzc.backend.lms.domains.notification.application.service;

import com.mzc.backend.lms.domains.notification.application.port.in.DeadLetterQueueManagementUseCase;
import com.mzc.backend.lms.domains.notification.application.port.out.NotificationQueuePort;
import com.mzc.backend.lms.domains.notification.domain.model.DeadLetterMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Dead Letter Queue 관리 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeadLetterQueueManagementService implements DeadLetterQueueManagementUseCase {

    private final NotificationQueuePort queuePort;

    @Override
    public List<DeadLetterMessage> getDeadLetterMessages(int offset, int limit) {
        return queuePort.getDeadLetterMessages(offset, limit);
    }

    @Override
    public long getDeadLetterQueueSize() {
        return queuePort.getDeadLetterQueueSize();
    }

    @Override
    public Optional<DeadLetterMessage> getDeadLetterMessage(String messageId) {
        return queuePort.getDeadLetterMessage(messageId);
    }

    @Override
    public boolean removeDeadLetterMessage(String messageId) {
        log.info("DLQ 메시지 삭제: messageId={}", messageId);
        return queuePort.removeFromDeadLetterQueue(messageId);
    }

    @Override
    public boolean reprocessDeadLetterMessage(String messageId) {
        log.info("DLQ 메시지 재처리: messageId={}", messageId);
        return queuePort.reprocessDeadLetterMessage(messageId);
    }

    @Override
    public int[] reprocessAllDeadLetterMessages() {
        log.info("모든 DLQ 메시지 재처리 시작");

        long totalCount = queuePort.getDeadLetterQueueSize();
        if (totalCount == 0) {
            return new int[]{0, 0};
        }

        List<DeadLetterMessage> messages = queuePort.getDeadLetterMessages(0, (int) totalCount);

        int successCount = 0;
        int failCount = 0;

        for (DeadLetterMessage message : messages) {
            if (queuePort.reprocessDeadLetterMessage(message.getMessageId())) {
                successCount++;
            } else {
                failCount++;
            }
        }

        log.info("모든 DLQ 메시지 재처리 완료: success={}, fail={}", successCount, failCount);
        return new int[]{successCount, failCount};
    }
}

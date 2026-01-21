package com.mzc.backend.lms.domains.notification.adapter.out.queue;

import com.mzc.backend.lms.domains.notification.adapter.out.queue.config.NotificationRedisConfig;
import com.mzc.backend.lms.domains.notification.domain.model.BatchNotificationMessage;
import com.mzc.backend.lms.domains.notification.domain.model.DeadLetterMessage;
import com.mzc.backend.lms.domains.notification.domain.model.NotificationMessage;
import com.mzc.backend.lms.domains.notification.application.port.out.NotificationQueuePort;
import com.mzc.backend.lms.domains.notification.exception.NotificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 알림 큐 Adapter (Redis 기반)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationQueueAdapter implements NotificationQueuePort {

    @Qualifier("notificationRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void publish(NotificationMessage message) {
        try {
            redisTemplate.opsForList().leftPush(
                    NotificationRedisConfig.NOTIFICATION_QUEUE_KEY,
                    message
            );
            log.debug("알림 메시지 큐 추가: recipientId={}", message.getRecipientId());
        } catch (Exception e) {
            log.error("알림 메시지 큐 추가 실패: {}", e.getMessage(), e);
            throw NotificationException.sendFailed(e);
        }
    }

    @Override
    public void publishBatch(BatchNotificationMessage message) {
        try {
            redisTemplate.opsForList().leftPush(
                    NotificationRedisConfig.BATCH_NOTIFICATION_QUEUE_KEY,
                    message
            );
            log.debug("배치 알림 메시지 큐 추가: batchId={}, recipientCount={}",
                    message.getBatchId(), message.getRecipientCount());
        } catch (Exception e) {
            log.error("배치 알림 메시지 큐 추가 실패: {}", e.getMessage(), e);
            throw NotificationException.sendFailed(e);
        }
    }

    @Override
    public Optional<NotificationMessage> dequeue(long timeoutSeconds) {
        try {
            Object result = redisTemplate.opsForList().rightPop(
                    NotificationRedisConfig.NOTIFICATION_QUEUE_KEY,
                    timeoutSeconds,
                    TimeUnit.SECONDS
            );

            if (result == null) {
                return Optional.empty();
            }

            log.debug("큐에서 메시지 수신: type={}, value={}", result.getClass().getName(), result);

            if (result instanceof NotificationMessage) {
                return Optional.of((NotificationMessage) result);
            }

            if (result instanceof java.util.Map) {
                log.warn("메시지가 Map으로 역직렬화됨, 수동 변환 시도: {}", result);
                try {
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> map = (java.util.Map<String, Object>) result;
                    NotificationMessage message = convertMapToNotificationMessage(map);
                    return Optional.of(message);
                } catch (Exception e) {
                    log.error("Map -> NotificationMessage 변환 실패: {}", e.getMessage(), e);
                    return Optional.empty();
                }
            }

            log.warn("알 수 없는 메시지 타입, 메시지 폐기: {}", result.getClass().getName());
            return Optional.empty();
        } catch (Exception e) {
            log.error("알림 메시지 큐 조회 실패: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<BatchNotificationMessage> dequeueBatch(long timeoutSeconds) {
        try {
            Object result = redisTemplate.opsForList().rightPop(
                    NotificationRedisConfig.BATCH_NOTIFICATION_QUEUE_KEY,
                    timeoutSeconds,
                    TimeUnit.SECONDS
            );
            if (result instanceof BatchNotificationMessage) {
                return Optional.of((BatchNotificationMessage) result);
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("배치 알림 메시지 큐 조회 실패: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    private NotificationMessage convertMapToNotificationMessage(java.util.Map<String, Object> map) {
        return NotificationMessage.builder()
                .typeId(map.get("typeId") != null ? ((Number) map.get("typeId")).intValue() : null)
                .senderId(map.get("senderId") != null ? ((Number) map.get("senderId")).longValue() : null)
                .recipientId(map.get("recipientId") != null ? ((Number) map.get("recipientId")).longValue() : null)
                .courseId(map.get("courseId") != null ? ((Number) map.get("courseId")).longValue() : null)
                .relatedEntityType((String) map.get("relatedEntityType"))
                .relatedEntityId(map.get("relatedEntityId") != null ? ((Number) map.get("relatedEntityId")).longValue() : null)
                .title((String) map.get("title"))
                .message((String) map.get("message"))
                .actionUrl((String) map.get("actionUrl"))
                .build();
    }

    // ===== Dead Letter Queue 구현 =====

    @Override
    public void publishToDeadLetterQueue(NotificationMessage message, String failureReason, int retryCount) {
        try {
            DeadLetterMessage dlqMessage = DeadLetterMessage.fromNotification(message, failureReason, retryCount);

            // Hash에 메시지 저장 (messageId를 키로 사용)
            redisTemplate.opsForHash().put(
                    NotificationRedisConfig.DEAD_LETTER_QUEUE_KEY,
                    dlqMessage.getMessageId(),
                    dlqMessage
            );

            // 인덱스 리스트에 messageId 추가 (순서 유지용)
            redisTemplate.opsForList().leftPush(
                    NotificationRedisConfig.DEAD_LETTER_INDEX_KEY,
                    dlqMessage.getMessageId()
            );

            log.warn("알림 메시지 DLQ 저장: messageId={}, recipientId={}, reason={}",
                    dlqMessage.getMessageId(), message.getRecipientId(), failureReason);
        } catch (Exception e) {
            log.error("DLQ 저장 실패: {}", e.getMessage(), e);
        }
    }

    @Override
    public void publishBatchToDeadLetterQueue(BatchNotificationMessage message, String failureReason, int retryCount) {
        try {
            DeadLetterMessage dlqMessage = DeadLetterMessage.fromBatchNotification(message, failureReason, retryCount);

            redisTemplate.opsForHash().put(
                    NotificationRedisConfig.DEAD_LETTER_QUEUE_KEY,
                    dlqMessage.getMessageId(),
                    dlqMessage
            );

            redisTemplate.opsForList().leftPush(
                    NotificationRedisConfig.DEAD_LETTER_INDEX_KEY,
                    dlqMessage.getMessageId()
            );

            log.warn("배치 알림 메시지 DLQ 저장: messageId={}, batchId={}, reason={}",
                    dlqMessage.getMessageId(), message.getBatchId(), failureReason);
        } catch (Exception e) {
            log.error("배치 DLQ 저장 실패: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<DeadLetterMessage> getDeadLetterMessages(int offset, int limit) {
        try {
            // 인덱스에서 messageId 목록 조회
            List<Object> messageIds = redisTemplate.opsForList().range(
                    NotificationRedisConfig.DEAD_LETTER_INDEX_KEY,
                    offset,
                    offset + limit - 1
            );

            if (messageIds == null || messageIds.isEmpty()) {
                return new ArrayList<>();
            }

            List<DeadLetterMessage> messages = new ArrayList<>();
            for (Object messageId : messageIds) {
                Object result = redisTemplate.opsForHash().get(
                        NotificationRedisConfig.DEAD_LETTER_QUEUE_KEY,
                        messageId.toString()
                );
                if (result instanceof DeadLetterMessage) {
                    messages.add((DeadLetterMessage) result);
                }
            }

            return messages;
        } catch (Exception e) {
            log.error("DLQ 메시지 목록 조회 실패: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public long getDeadLetterQueueSize() {
        try {
            Long size = redisTemplate.opsForHash().size(NotificationRedisConfig.DEAD_LETTER_QUEUE_KEY);
            return size != null ? size : 0;
        } catch (Exception e) {
            log.error("DLQ 크기 조회 실패: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public Optional<DeadLetterMessage> getDeadLetterMessage(String messageId) {
        try {
            Object result = redisTemplate.opsForHash().get(
                    NotificationRedisConfig.DEAD_LETTER_QUEUE_KEY,
                    messageId
            );
            if (result instanceof DeadLetterMessage) {
                return Optional.of((DeadLetterMessage) result);
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("DLQ 메시지 조회 실패: messageId={}, error={}", messageId, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean removeFromDeadLetterQueue(String messageId) {
        try {
            // Hash에서 삭제
            Long deleted = redisTemplate.opsForHash().delete(
                    NotificationRedisConfig.DEAD_LETTER_QUEUE_KEY,
                    messageId
            );

            // 인덱스에서도 삭제
            redisTemplate.opsForList().remove(
                    NotificationRedisConfig.DEAD_LETTER_INDEX_KEY,
                    1,
                    messageId
            );

            boolean success = deleted != null && deleted > 0;
            if (success) {
                log.info("DLQ 메시지 삭제 완료: messageId={}", messageId);
            }
            return success;
        } catch (Exception e) {
            log.error("DLQ 메시지 삭제 실패: messageId={}, error={}", messageId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean reprocessDeadLetterMessage(String messageId) {
        try {
            Optional<DeadLetterMessage> dlqMessageOpt = getDeadLetterMessage(messageId);
            if (dlqMessageOpt.isEmpty()) {
                log.warn("재처리할 DLQ 메시지 없음: messageId={}", messageId);
                return false;
            }

            DeadLetterMessage dlqMessage = dlqMessageOpt.get();

            // 메시지 타입에 따라 원본 큐로 재발행
            if (dlqMessage.isSingleMessage() && dlqMessage.getNotificationMessage() != null) {
                publish(dlqMessage.getNotificationMessage());
                log.info("DLQ 단일 메시지 재발행: messageId={}, recipientId={}",
                        messageId, dlqMessage.getNotificationMessage().getRecipientId());
            } else if (dlqMessage.isBatchMessage() && dlqMessage.getBatchNotificationMessage() != null) {
                publishBatch(dlqMessage.getBatchNotificationMessage());
                log.info("DLQ 배치 메시지 재발행: messageId={}, batchId={}",
                        messageId, dlqMessage.getBatchNotificationMessage().getBatchId());
            } else {
                log.error("DLQ 메시지 타입 불일치: messageId={}, type={}", messageId, dlqMessage.getMessageType());
                return false;
            }

            // DLQ에서 삭제
            removeFromDeadLetterQueue(messageId);
            return true;
        } catch (Exception e) {
            log.error("DLQ 메시지 재처리 실패: messageId={}, error={}", messageId, e.getMessage(), e);
            return false;
        }
    }
}

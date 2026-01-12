package com.mzc.backend.lms.domains.notification.adapter.out.queue;

import com.mzc.backend.lms.domains.notification.adapter.out.queue.config.NotificationRedisConfig;
import com.mzc.backend.lms.domains.notification.adapter.out.queue.dto.BatchNotificationMessage;
import com.mzc.backend.lms.domains.notification.adapter.out.queue.dto.NotificationMessage;
import com.mzc.backend.lms.domains.notification.application.port.out.NotificationQueuePort;
import com.mzc.backend.lms.domains.notification.exception.NotificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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
}

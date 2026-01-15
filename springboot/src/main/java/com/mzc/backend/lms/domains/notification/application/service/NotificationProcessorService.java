package com.mzc.backend.lms.domains.notification.application.service;

import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.Notification;
import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.NotificationBatch;
import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.NotificationType;
import com.mzc.backend.lms.domains.notification.domain.model.BatchNotificationMessage;
import com.mzc.backend.lms.domains.notification.domain.model.NotificationMessage;
import com.mzc.backend.lms.domains.notification.application.port.in.NotificationProcessor;
import com.mzc.backend.lms.domains.notification.application.port.out.NotificationBatchRepositoryPort;
import com.mzc.backend.lms.domains.notification.application.port.out.NotificationRepositoryPort;
import com.mzc.backend.lms.domains.notification.application.port.out.NotificationTypeRepositoryPort;
import com.mzc.backend.lms.domains.notification.application.port.out.UserLookupPort;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 알림 처리 서비스
 * 큐에서 받은 메시지를 실제 알림 엔티티로 변환하여 저장
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationProcessorService implements NotificationProcessor {

    private final NotificationRepositoryPort notificationRepositoryPort;
    private final NotificationTypeRepositoryPort notificationTypeRepositoryPort;
    private final NotificationBatchRepositoryPort notificationBatchRepositoryPort;
    private final UserLookupPort userLookupPort;

    @Override
    public void process(NotificationMessage message) {
        try {
            NotificationType type = notificationTypeRepositoryPort.findById(message.getTypeId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "알림 타입을 찾을 수 없습니다: " + message.getTypeId()));

            User sender = message.getSenderId() != null ?
                    userLookupPort.findById(message.getSenderId()).orElse(null) : null;

            User recipient = userLookupPort.findById(message.getRecipientId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "수신자를 찾을 수 없습니다: " + message.getRecipientId()));

            Notification notification = Notification.createWithRelatedEntity(
                    type, sender, recipient,
                    message.getTitle(), message.getMessage(),
                    message.getRelatedEntityType(), message.getRelatedEntityId(),
                    message.getCourseId(), message.getActionUrl()
            );

            notificationRepositoryPort.save(notification);
            log.debug("알림 저장 완료: recipientId={}", message.getRecipientId());

        } catch (Exception e) {
            log.error("알림 처리 실패: recipientId={}, error={}",
                    message.getRecipientId(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void processBatch(BatchNotificationMessage message) {
        NotificationBatch batch = null;

        try {
            // 배치 상태 업데이트: 처리 중
            if (message.getBatchId() != null) {
                batch = notificationBatchRepositoryPort.findById(message.getBatchId()).orElse(null);
                if (batch != null) {
                    batch.startProcessing();
                    notificationBatchRepositoryPort.save(batch);
                }
            }

            NotificationType type = notificationTypeRepositoryPort.findById(message.getTypeId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "알림 타입을 찾을 수 없습니다: " + message.getTypeId()));

            User sender = message.getSenderId() != null ?
                    userLookupPort.findById(message.getSenderId()).orElse(null) : null;

            List<Notification> notifications = new ArrayList<>();
            int successCount = 0;
            int failCount = 0;

            for (Long recipientId : message.getRecipientIds()) {
                try {
                    User recipient = userLookupPort.findById(recipientId).orElse(null);
                    if (recipient == null) {
                        log.warn("수신자를 찾을 수 없습니다: {}", recipientId);
                        failCount++;
                        continue;
                    }

                    Notification notification = Notification.createWithRelatedEntity(
                            type, sender, recipient,
                            message.getTitle(), message.getMessage(),
                            message.getRelatedEntityType(), message.getRelatedEntityId(),
                            message.getCourseId(), message.getActionUrl()
                    );
                    notifications.add(notification);
                    successCount++;

                } catch (Exception e) {
                    log.error("개별 알림 생성 실패: recipientId={}", recipientId, e);
                    failCount++;
                }
            }

            // 일괄 저장
            if (!notifications.isEmpty()) {
                notificationRepositoryPort.saveAll(notifications);
            }

            // 배치 상태 업데이트: 완료
            if (batch != null) {
                batch.complete();
                notificationBatchRepositoryPort.save(batch);
            }

            log.info("배치 알림 처리 완료: batchId={}, success={}, fail={}",
                    message.getBatchId(), successCount, failCount);

        } catch (Exception e) {
            // 배치 상태 업데이트: 실패
            if (batch != null) {
                batch.fail(e.getMessage());
                notificationBatchRepositoryPort.save(batch);
            }
            log.error("배치 알림 처리 실패: batchId={}, error={}",
                    message.getBatchId(), e.getMessage(), e);
            throw e;
        }
    }
}

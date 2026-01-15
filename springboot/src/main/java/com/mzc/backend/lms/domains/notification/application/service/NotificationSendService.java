package com.mzc.backend.lms.domains.notification.application.service;

import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.NotificationBatch;
import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.NotificationType;
import com.mzc.backend.lms.domains.notification.adapter.out.queue.dto.BatchNotificationMessage;
import com.mzc.backend.lms.domains.notification.adapter.out.queue.dto.NotificationMessage;
import com.mzc.backend.lms.common.notification.event.NotificationEventType;
import com.mzc.backend.lms.domains.notification.application.port.in.SendNotificationUseCase;
import com.mzc.backend.lms.domains.notification.application.port.out.NotificationBatchRepositoryPort;
import com.mzc.backend.lms.domains.notification.application.port.out.NotificationQueuePort;
import com.mzc.backend.lms.domains.notification.application.port.out.NotificationTypeRepositoryPort;
import com.mzc.backend.lms.domains.notification.application.port.out.UserLookupPort;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 알림 발송 서비스
 * NotificationQueuePort를 통해 Redis 큐에 알림을 추가
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationSendService implements SendNotificationUseCase {

    private final NotificationQueuePort notificationQueuePort;
    private final NotificationTypeRepositoryPort notificationTypeRepositoryPort;
    private final NotificationBatchRepositoryPort notificationBatchRepositoryPort;
    private final UserLookupPort userLookupPort;

    @Override
    @Async
    public void send(NotificationEventType eventType, Long senderId, Long recipientId,
                     String title, String message) {
        try {
            Integer typeId = getTypeId(eventType);
            if (typeId == null) {
                log.warn("알림 타입을 찾을 수 없음: {}", eventType.getTypeCode());
                return;
            }

            NotificationMessage notificationMessage = NotificationMessage.builder()
                    .typeId(typeId)
                    .senderId(senderId)
                    .recipientId(recipientId)
                    .title(title)
                    .message(message)
                    .build();

            notificationQueuePort.publish(notificationMessage);
            log.debug("알림 발행 완료: type={}, recipientId={}", eventType, recipientId);

        } catch (Exception e) {
            log.error("알림 발행 실패: type={}, recipientId={}, error={}",
                    eventType, recipientId, e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendForCourse(NotificationEventType eventType, Long senderId, Long recipientId,
                              Long courseId, String title, String message) {
        try {
            Integer typeId = getTypeId(eventType);
            if (typeId == null) {
                log.warn("알림 타입을 찾을 수 없음: {}", eventType.getTypeCode());
                return;
            }

            NotificationMessage notificationMessage = NotificationMessage.forCourse(
                    typeId, senderId, recipientId, courseId, title, message
            );

            notificationQueuePort.publish(notificationMessage);
            log.debug("강의 알림 발행 완료: type={}, courseId={}, recipientId={}",
                    eventType, courseId, recipientId);

        } catch (Exception e) {
            log.error("강의 알림 발행 실패: type={}, courseId={}, error={}",
                    eventType, courseId, e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendWithEntity(NotificationEventType eventType, Long senderId, Long recipientId,
                               String relatedEntityType, Long relatedEntityId, Long courseId,
                               String title, String message, String actionUrl) {
        try {
            Integer typeId = getTypeId(eventType);
            if (typeId == null) {
                log.warn("알림 타입을 찾을 수 없음: {}", eventType.getTypeCode());
                return;
            }

            NotificationMessage notificationMessage = NotificationMessage.withRelatedEntity(
                    typeId, senderId, recipientId, relatedEntityType, relatedEntityId,
                    courseId, title, message, actionUrl
            );

            notificationQueuePort.publish(notificationMessage);
            log.debug("엔티티 알림 발행 완료: type={}, entityType={}, entityId={}",
                    eventType, relatedEntityType, relatedEntityId);

        } catch (Exception e) {
            log.error("엔티티 알림 발행 실패: type={}, entityType={}, error={}",
                    eventType, relatedEntityType, e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void sendBatch(NotificationEventType eventType, Long senderId, List<Long> recipientIds,
                          Long courseId, String title, String message) {
        sendBatchWithEntity(eventType, senderId, recipientIds, null, null,
                courseId, title, message, null);
    }

    @Override
    @Transactional
    public void sendBatchWithEntity(NotificationEventType eventType, Long senderId, List<Long> recipientIds,
                                    String relatedEntityType, Long relatedEntityId, Long courseId,
                                    String title, String message, String actionUrl) {
        try {
            if (recipientIds == null || recipientIds.isEmpty()) {
                log.warn("배치 알림 수신자 목록이 비어있음: type={}", eventType);
                return;
            }

            Optional<NotificationType> typeOpt = notificationTypeRepositoryPort.findByTypeCode(eventType.getTypeCode());
            if (typeOpt.isEmpty()) {
                log.warn("알림 타입을 찾을 수 없음: {}", eventType.getTypeCode());
                return;
            }

            NotificationType notificationType = typeOpt.get();
            User sender = senderId != null ? userLookupPort.findById(senderId).orElse(null) : null;

            // 배치 레코드 생성
            NotificationBatch batch = NotificationBatch.create(
                    notificationType, sender, courseId, title, message, recipientIds.size()
            );
            batch = notificationBatchRepositoryPort.save(batch);

            // 배치 메시지 생성 및 큐에 추가
            BatchNotificationMessage batchMessage = BatchNotificationMessage.builder()
                    .batchId(batch.getId())
                    .typeId(notificationType.getId())
                    .senderId(senderId)
                    .recipientIds(recipientIds)
                    .courseId(courseId)
                    .relatedEntityType(relatedEntityType)
                    .relatedEntityId(relatedEntityId)
                    .title(title)
                    .message(message)
                    .actionUrl(actionUrl)
                    .build();

            notificationQueuePort.publishBatch(batchMessage);
            log.info("배치 알림 발행 완료: type={}, batchId={}, recipientCount={}",
                    eventType, batch.getId(), recipientIds.size());

        } catch (Exception e) {
            log.error("배치 알림 발행 실패: type={}, error={}", eventType, e.getMessage(), e);
        }
    }

    /**
     * 이벤트 타입으로 알림 타입 ID 조회
     */
    private Integer getTypeId(NotificationEventType eventType) {
        return notificationTypeRepositoryPort.findByTypeCode(eventType.getTypeCode())
                .map(NotificationType::getId)
                .orElse(null);
    }
}

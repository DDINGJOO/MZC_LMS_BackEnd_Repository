package com.mzc.lms.notification.application.service;

import com.mzc.lms.notification.adapter.in.web.dto.NotificationSendRequest;
import com.mzc.lms.notification.application.port.in.NotificationUseCase;
import com.mzc.lms.notification.domain.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationUseCase notificationUseCase;

    @KafkaListener(topics = "${notification.kafka.topics.course-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleCourseEvent(NotificationEvent event) {
        log.info("Received course event: {}", event.getEventId());
        processEvent(event);
    }

    @KafkaListener(topics = "${notification.kafka.topics.enrollment-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleEnrollmentEvent(NotificationEvent event) {
        log.info("Received enrollment event: {}", event.getEventId());
        processEvent(event);
    }

    @KafkaListener(topics = "${notification.kafka.topics.assessment-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleAssessmentEvent(NotificationEvent event) {
        log.info("Received assessment event: {}", event.getEventId());
        processEvent(event);
    }

    @KafkaListener(topics = "${notification.kafka.topics.board-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleBoardEvent(NotificationEvent event) {
        log.info("Received board event: {}", event.getEventId());
        processEvent(event);
    }

    private void processEvent(NotificationEvent event) {
        try {
            NotificationSendRequest request = NotificationSendRequest.builder()
                    .userId(event.getUserId())
                    .type(event.getType())
                    .channel(event.getChannel())
                    .title(event.getTitle())
                    .content(event.getContent())
                    .data(event.getData())
                    .build();

            notificationUseCase.sendNotification(request);
            log.info("Successfully processed notification event: {}", event.getEventId());
        } catch (Exception e) {
            log.error("Failed to process notification event: {}", event.getEventId(), e);
        }
    }
}

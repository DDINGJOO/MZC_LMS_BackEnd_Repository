package com.mzc.lms.notification.adapter.in.web;

import com.mzc.lms.notification.adapter.in.web.dto.NotificationResponse;
import com.mzc.lms.notification.adapter.in.web.dto.NotificationSendRequest;
import com.mzc.lms.notification.application.port.in.NotificationUseCase;
import com.mzc.lms.notification.domain.model.Notification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "알림 API")
public class NotificationController {

    private final NotificationUseCase notificationUseCase;

    @PostMapping
    @Operation(summary = "알림 전송", description = "사용자에게 알림을 전송합니다.")
    public ResponseEntity<NotificationResponse> sendNotification(@Valid @RequestBody NotificationSendRequest request) {
        log.debug("Sending notification to user: {}", request.getUserId());
        Notification notification = notificationUseCase.sendNotification(request);

        if (notification == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(NotificationResponse.from(notification));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자 알림 조회", description = "특정 사용자의 알림 목록을 조회합니다.")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("Getting notifications for user: {}, page: {}, size: {}", userId, page, size);
        List<Notification> notifications = notificationUseCase.getNotifications(userId, page, size);
        return ResponseEntity.ok(NotificationResponse.from(notifications));
    }

    @GetMapping("/{id}")
    @Operation(summary = "알림 상세 조회", description = "알림 상세 정보를 조회합니다.")
    public ResponseEntity<NotificationResponse> getNotification(@PathVariable Long id) {
        log.debug("Getting notification: {}", id);
        Notification notification = notificationUseCase.getNotification(id);
        return ResponseEntity.ok(NotificationResponse.from(notification));
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "알림 읽음 처리", description = "알림을 읽음 상태로 변경합니다.")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        log.debug("Marking notification as read: {}", id);
        notificationUseCase.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/user/{userId}/read-all")
    @Operation(summary = "모든 알림 읽음 처리", description = "사용자의 모든 알림을 읽음 상태로 변경합니다.")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long userId) {
        log.debug("Marking all notifications as read for user: {}", userId);
        notificationUseCase.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/unread-count")
    @Operation(summary = "읽지 않은 알림 개수", description = "사용자의 읽지 않은 알림 개수를 조회합니다.")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long userId) {
        log.debug("Getting unread count for user: {}", userId);
        long count = notificationUseCase.getUnreadCount(userId);
        return ResponseEntity.ok(count);
    }
}

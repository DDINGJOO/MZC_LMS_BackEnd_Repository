package com.mzc.backend.lms.domains.notification.adapter.in.web;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.notification.adapter.in.web.dto.BulkDeleteResponseDto;
import com.mzc.backend.lms.domains.notification.adapter.in.web.dto.BulkUpdateResponseDto;
import com.mzc.backend.lms.domains.notification.adapter.in.web.dto.NotificationCursorResponseDto;
import com.mzc.backend.lms.domains.notification.adapter.in.web.dto.NotificationResponseDto;
import com.mzc.backend.lms.domains.notification.adapter.in.web.dto.UnreadCountResponseDto;
import com.mzc.backend.lms.domains.notification.application.port.in.GetNotificationsUseCase;
import com.mzc.backend.lms.domains.notification.application.port.in.ManageNotificationUseCase;
import com.mzc.backend.lms.domains.user.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 알림 컨트롤러
 * (Hexagonal Architecture - Inbound Adapter)
 */
@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final GetNotificationsUseCase getNotificationsUseCase;
    private final ManageNotificationUseCase manageNotificationUseCase;

    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    /**
     * 알림 목록 조회 (커서 기반)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<NotificationCursorResponseDto>> getNotifications(
            @AuthenticationPrincipal Long userId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "false") boolean unreadOnly) {
        validateUserId(userId);
        int validSize = validateSize(size);

        NotificationCursorResponseDto response;
        if (unreadOnly) {
            response = getNotificationsUseCase.getUnreadNotifications(userId, cursor, validSize);
        } else {
            response = getNotificationsUseCase.getNotifications(userId, cursor, validSize);
        }

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 알림 상세 조회
     */
    @GetMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<NotificationResponseDto>> getNotification(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long notificationId) {
        validateUserId(userId);

        NotificationResponseDto response = getNotificationsUseCase.getNotification(userId, notificationId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 읽지 않은 알림 개수 조회
     */
    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<UnreadCountResponseDto>> getUnreadCount(
            @AuthenticationPrincipal Long userId) {
        validateUserId(userId);

        long unreadCount = getNotificationsUseCase.getUnreadCount(userId);

        return ResponseEntity.ok(ApiResponse.success(new UnreadCountResponseDto(unreadCount)));
    }

    /**
     * 알림 읽음 처리
     */
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<NotificationResponseDto>> markAsRead(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long notificationId) {
        validateUserId(userId);

        NotificationResponseDto response = manageNotificationUseCase.markAsRead(userId, notificationId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 모든 알림 읽음 처리
     */
    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<BulkUpdateResponseDto>> markAllAsRead(
            @AuthenticationPrincipal Long userId) {
        validateUserId(userId);

        int updatedCount = manageNotificationUseCase.markAllAsRead(userId);

        return ResponseEntity.ok(ApiResponse.success(
                new BulkUpdateResponseDto("모든 알림이 읽음 처리되었습니다.", updatedCount)));
    }

    /**
     * 알림 삭제
     */
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<ApiResponse.MessageResponse>> deleteNotification(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long notificationId) {
        validateUserId(userId);

        manageNotificationUseCase.deleteNotification(userId, notificationId);

        return ResponseEntity.ok(ApiResponse.successMessage("알림이 삭제되었습니다."));
    }

    /**
     * 읽은 알림 일괄 삭제
     */
    @DeleteMapping("/read")
    public ResponseEntity<ApiResponse<BulkDeleteResponseDto>> deleteReadNotifications(
            @AuthenticationPrincipal Long userId) {
        validateUserId(userId);

        int deletedCount = manageNotificationUseCase.deleteReadNotifications(userId);

        return ResponseEntity.ok(ApiResponse.success(
                new BulkDeleteResponseDto("읽은 알림이 삭제되었습니다.", deletedCount)));
    }

    /**
     * 모든 알림 삭제
     */
    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse<BulkDeleteResponseDto>> deleteAllNotifications(
            @AuthenticationPrincipal Long userId) {
        validateUserId(userId);

        int deletedCount = manageNotificationUseCase.deleteAllNotifications(userId);

        return ResponseEntity.ok(ApiResponse.success(
                new BulkDeleteResponseDto("모든 알림이 삭제되었습니다.", deletedCount)));
    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw AuthException.unauthorized();
        }
    }

    private int validateSize(int size) {
        return Math.min(Math.max(size, 1), MAX_SIZE);
    }
}

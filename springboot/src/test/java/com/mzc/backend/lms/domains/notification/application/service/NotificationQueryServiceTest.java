package com.mzc.backend.lms.domains.notification.application.service;

import com.mzc.backend.lms.domains.notification.adapter.in.web.dto.NotificationCursorResponseDto;
import com.mzc.backend.lms.domains.notification.adapter.in.web.dto.NotificationResponseDto;
import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.Notification;
import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.NotificationType;
import com.mzc.backend.lms.domains.notification.application.port.out.NotificationRepositoryPort;
import com.mzc.backend.lms.domains.notification.exception.NotificationErrorCode;
import com.mzc.backend.lms.domains.notification.exception.NotificationException;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.User;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationQueryService Tests")
class NotificationQueryServiceTest {

    @Mock
    private NotificationRepositoryPort notificationRepositoryPort;

    @InjectMocks
    private NotificationQueryService notificationQueryService;

    private User recipient;
    private User sender;
    private NotificationType notificationType;
    private Notification notification1;
    private Notification notification2;
    private Notification notification3;

    @BeforeEach
    void setUp() {
        // Create recipient
        recipient = User.builder()
                .id(1L)
                .email("recipient@test.com")
                .password("password")
                .build();

        // Create sender with profile
        sender = User.builder()
                .id(2L)
                .email("sender@test.com")
                .password("password")
                .build();

        // Use reflection to set userProfile since it's a mapped field
        UserProfile senderProfile = UserProfile.builder()
                .user(sender)
                .name("Sender Name")
                .build();
        setUserProfile(sender, senderProfile);

        // Create notification type
        notificationType = NotificationType.builder()
                .typeCode("SYSTEM")
                .typeName("System Notification")
                .category("INFO")
                .defaultMessageTemplate("Default template")
                .build();

        // Create notifications
        notification1 = Notification.builder()
                .notificationType(notificationType)
                .sender(sender)
                .recipient(recipient)
                .title("Notification 1")
                .message("Message 1")
                .build();
        setId(notification1, 3L);
        setIsRead(notification1, false);
        setCreatedAt(notification1, LocalDateTime.now());

        notification2 = Notification.builder()
                .notificationType(notificationType)
                .sender(sender)
                .recipient(recipient)
                .title("Notification 2")
                .message("Message 2")
                .build();
        setId(notification2, 2L);
        setIsRead(notification2, true);
        setCreatedAt(notification2, LocalDateTime.now().minusHours(1));

        notification3 = Notification.builder()
                .notificationType(notificationType)
                .sender(sender)
                .recipient(recipient)
                .title("Notification 3")
                .message("Message 3")
                .build();
        setId(notification3, 1L);
        setIsRead(notification3, false);
        setCreatedAt(notification3, LocalDateTime.now().minusHours(2));
    }

    @Nested
    @DisplayName("getNotifications Tests")
    class GetNotificationsTests {

        @Test
        @DisplayName("Given userId and no cursor When getNotifications Then returns all notifications with unread count")
        void getNotifications_withoutCursor_shouldReturnNotificationsWithUnreadCount() {
            // Given
            Long userId = 1L;
            int size = 10;
            List<Notification> notifications = Arrays.asList(notification1, notification2, notification3);
            Long unreadCount = 2L;

            given(notificationRepositoryPort.findByRecipientIdOrderByIdDesc(eq(userId), any(Pageable.class)))
                    .willReturn(notifications);
            given(notificationRepositoryPort.countUnreadByRecipientId(userId))
                    .willReturn(unreadCount);

            // When
            NotificationCursorResponseDto result = notificationQueryService.getNotifications(userId, null, size);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNotifications()).hasSize(3);
            assertThat(result.getUnreadCount()).isEqualTo(unreadCount);
            assertThat(result.isHasNext()).isFalse();
            assertThat(result.getSize()).isEqualTo(size);

            then(notificationRepositoryPort).should().findByRecipientIdOrderByIdDesc(eq(userId), any(Pageable.class));
            then(notificationRepositoryPort).should().countUnreadByRecipientId(userId);
        }

        @Test
        @DisplayName("Given userId and cursor When getNotifications Then returns notifications without unread count")
        void getNotifications_withCursor_shouldReturnNotificationsWithoutUnreadCount() {
            // Given
            Long userId = 1L;
            Long cursor = 3L;
            int size = 10;
            List<Notification> notifications = Arrays.asList(notification2, notification3);

            given(notificationRepositoryPort.findByRecipientIdAndIdLessThanOrderByIdDesc(eq(userId), eq(cursor), any(Pageable.class)))
                    .willReturn(notifications);

            // When
            NotificationCursorResponseDto result = notificationQueryService.getNotifications(userId, cursor, size);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNotifications()).hasSize(2);
            assertThat(result.getUnreadCount()).isNull();
            assertThat(result.isHasNext()).isFalse();

            then(notificationRepositoryPort).should().findByRecipientIdAndIdLessThanOrderByIdDesc(eq(userId), eq(cursor), any(Pageable.class));
            then(notificationRepositoryPort).should(never()).countUnreadByRecipientId(userId);
        }

        @Test
        @DisplayName("Given more notifications than size When getNotifications Then returns hasNext true")
        void getNotifications_withMoreThanSize_shouldReturnHasNextTrue() {
            // Given
            Long userId = 1L;
            int size = 2;
            List<Notification> notifications = Arrays.asList(notification1, notification2, notification3);

            given(notificationRepositoryPort.findByRecipientIdOrderByIdDesc(eq(userId), any(Pageable.class)))
                    .willReturn(notifications);
            given(notificationRepositoryPort.countUnreadByRecipientId(userId))
                    .willReturn(2L);

            // When
            NotificationCursorResponseDto result = notificationQueryService.getNotifications(userId, null, size);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNotifications()).hasSize(size);
            assertThat(result.isHasNext()).isTrue();
            assertThat(result.getNextCursor()).isEqualTo(2L);
        }

        @Test
        @DisplayName("Given empty result When getNotifications Then returns empty list")
        void getNotifications_withNoResults_shouldReturnEmptyList() {
            // Given
            Long userId = 1L;
            int size = 10;

            given(notificationRepositoryPort.findByRecipientIdOrderByIdDesc(eq(userId), any(Pageable.class)))
                    .willReturn(Collections.emptyList());
            given(notificationRepositoryPort.countUnreadByRecipientId(userId))
                    .willReturn(0L);

            // When
            NotificationCursorResponseDto result = notificationQueryService.getNotifications(userId, null, size);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNotifications()).isEmpty();
            assertThat(result.isHasNext()).isFalse();
            assertThat(result.getUnreadCount()).isEqualTo(0L);
        }
    }

    @Nested
    @DisplayName("getUnreadNotifications Tests")
    class GetUnreadNotificationsTests {

        @Test
        @DisplayName("Given userId and no cursor When getUnreadNotifications Then returns unread notifications with count")
        void getUnreadNotifications_withoutCursor_shouldReturnUnreadNotificationsWithCount() {
            // Given
            Long userId = 1L;
            int size = 10;
            List<Notification> unreadNotifications = Arrays.asList(notification1, notification3);
            Long unreadCount = 2L;

            given(notificationRepositoryPort.findUnreadByRecipientIdOrderByIdDesc(eq(userId), any(Pageable.class)))
                    .willReturn(unreadNotifications);
            given(notificationRepositoryPort.countUnreadByRecipientId(userId))
                    .willReturn(unreadCount);

            // When
            NotificationCursorResponseDto result = notificationQueryService.getUnreadNotifications(userId, null, size);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNotifications()).hasSize(2);
            assertThat(result.getUnreadCount()).isEqualTo(unreadCount);
            assertThat(result.isHasNext()).isFalse();

            then(notificationRepositoryPort).should().findUnreadByRecipientIdOrderByIdDesc(eq(userId), any(Pageable.class));
            then(notificationRepositoryPort).should().countUnreadByRecipientId(userId);
        }

        @Test
        @DisplayName("Given userId and cursor When getUnreadNotifications Then returns unread notifications without count")
        void getUnreadNotifications_withCursor_shouldReturnUnreadNotificationsWithoutCount() {
            // Given
            Long userId = 1L;
            Long cursor = 3L;
            int size = 10;
            List<Notification> unreadNotifications = Collections.singletonList(notification3);

            given(notificationRepositoryPort.findUnreadByRecipientIdAndIdLessThanOrderByIdDesc(eq(userId), eq(cursor), any(Pageable.class)))
                    .willReturn(unreadNotifications);

            // When
            NotificationCursorResponseDto result = notificationQueryService.getUnreadNotifications(userId, cursor, size);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNotifications()).hasSize(1);
            assertThat(result.getUnreadCount()).isNull();
            assertThat(result.isHasNext()).isFalse();

            then(notificationRepositoryPort).should().findUnreadByRecipientIdAndIdLessThanOrderByIdDesc(eq(userId), eq(cursor), any(Pageable.class));
            then(notificationRepositoryPort).should(never()).countUnreadByRecipientId(userId);
        }

        @Test
        @DisplayName("Given no unread notifications When getUnreadNotifications Then returns empty list")
        void getUnreadNotifications_withNoResults_shouldReturnEmptyList() {
            // Given
            Long userId = 1L;
            int size = 10;

            given(notificationRepositoryPort.findUnreadByRecipientIdOrderByIdDesc(eq(userId), any(Pageable.class)))
                    .willReturn(Collections.emptyList());
            given(notificationRepositoryPort.countUnreadByRecipientId(userId))
                    .willReturn(0L);

            // When
            NotificationCursorResponseDto result = notificationQueryService.getUnreadNotifications(userId, null, size);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNotifications()).isEmpty();
            assertThat(result.isHasNext()).isFalse();
            assertThat(result.getUnreadCount()).isEqualTo(0L);
        }
    }

    @Nested
    @DisplayName("getNotification Tests")
    class GetNotificationTests {

        @Test
        @DisplayName("Given valid userId and notificationId When getNotification Then returns notification and marks as read")
        void getNotification_withValidIds_shouldReturnNotificationAndMarkAsRead() {
            // Given
            Long userId = 1L;
            Long notificationId = 3L;

            given(notificationRepositoryPort.findById(notificationId))
                    .willReturn(Optional.of(notification1));
            given(notificationRepositoryPort.save(any(Notification.class)))
                    .willReturn(notification1);

            // When
            NotificationResponseDto result = notificationQueryService.getNotification(userId, notificationId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(notificationId);
            assertThat(result.getMessage()).isEqualTo("Message 1");

            then(notificationRepositoryPort).should().findById(notificationId);
            then(notificationRepositoryPort).should().save(notification1);
        }

        @Test
        @DisplayName("Given already read notification When getNotification Then returns notification without saving")
        void getNotification_withReadNotification_shouldNotSave() {
            // Given
            Long userId = 1L;
            Long notificationId = 2L;

            given(notificationRepositoryPort.findById(notificationId))
                    .willReturn(Optional.of(notification2));

            // When
            NotificationResponseDto result = notificationQueryService.getNotification(userId, notificationId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(notificationId);

            then(notificationRepositoryPort).should().findById(notificationId);
            then(notificationRepositoryPort).should(never()).save(any(Notification.class));
        }

        @Test
        @DisplayName("Given non-existent notificationId When getNotification Then throws NotificationException")
        void getNotification_withNonExistentId_shouldThrowException() {
            // Given
            Long userId = 1L;
            Long notificationId = 999L;

            given(notificationRepositoryPort.findById(notificationId))
                    .willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> notificationQueryService.getNotification(userId, notificationId))
                    .isInstanceOf(NotificationException.class)
                    .hasMessageContaining("알림 ID: " + notificationId)
                    .extracting("notificationErrorCode")
                    .isEqualTo(NotificationErrorCode.NOTIFICATION_NOT_FOUND);

            then(notificationRepositoryPort).should().findById(notificationId);
            then(notificationRepositoryPort).should(never()).save(any(Notification.class));
        }

        @Test
        @DisplayName("Given userId not owner of notification When getNotification Then throws NotificationException")
        void getNotification_withNotOwner_shouldThrowException() {
            // Given
            Long userId = 999L; // Different user
            Long notificationId = 3L;

            given(notificationRepositoryPort.findById(notificationId))
                    .willReturn(Optional.of(notification1));

            // When & Then
            assertThatThrownBy(() -> notificationQueryService.getNotification(userId, notificationId))
                    .isInstanceOf(NotificationException.class)
                    .hasMessageContaining("사용자 ID: " + userId)
                    .hasMessageContaining("알림 ID: " + notificationId)
                    .extracting("notificationErrorCode")
                    .isEqualTo(NotificationErrorCode.NOT_NOTIFICATION_OWNER);

            then(notificationRepositoryPort).should().findById(notificationId);
            then(notificationRepositoryPort).should(never()).save(any(Notification.class));
        }
    }

    @Nested
    @DisplayName("getUnreadCount Tests")
    class GetUnreadCountTests {

        @Test
        @DisplayName("Given userId with unread notifications When getUnreadCount Then returns correct count")
        void getUnreadCount_withUnreadNotifications_shouldReturnCount() {
            // Given
            Long userId = 1L;
            Long expectedCount = 5L;

            given(notificationRepositoryPort.countUnreadByRecipientId(userId))
                    .willReturn(expectedCount);

            // When
            long result = notificationQueryService.getUnreadCount(userId);

            // Then
            assertThat(result).isEqualTo(expectedCount);
            then(notificationRepositoryPort).should().countUnreadByRecipientId(userId);
        }

        @Test
        @DisplayName("Given userId with no unread notifications When getUnreadCount Then returns zero")
        void getUnreadCount_withNoUnreadNotifications_shouldReturnZero() {
            // Given
            Long userId = 1L;

            given(notificationRepositoryPort.countUnreadByRecipientId(userId))
                    .willReturn(0L);

            // When
            long result = notificationQueryService.getUnreadCount(userId);

            // Then
            assertThat(result).isEqualTo(0L);
            then(notificationRepositoryPort).should().countUnreadByRecipientId(userId);
        }
    }

    // Helper methods to set private fields using reflection
    private void setId(Notification notification, Long id) {
        try {
            java.lang.reflect.Field field = Notification.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(notification, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setIsRead(Notification notification, Boolean isRead) {
        try {
            java.lang.reflect.Field field = Notification.class.getDeclaredField("isRead");
            field.setAccessible(true);
            field.set(notification, isRead);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setCreatedAt(Notification notification, LocalDateTime createdAt) {
        try {
            java.lang.reflect.Field field = Notification.class.getDeclaredField("createdAt");
            field.setAccessible(true);
            field.set(notification, createdAt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setUserProfile(User user, UserProfile userProfile) {
        try {
            java.lang.reflect.Field field = User.class.getDeclaredField("userProfile");
            field.setAccessible(true);
            field.set(user, userProfile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

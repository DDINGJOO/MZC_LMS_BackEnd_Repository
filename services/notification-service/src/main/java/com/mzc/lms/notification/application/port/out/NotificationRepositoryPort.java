package com.mzc.lms.notification.application.port.out;

import com.mzc.lms.notification.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificationRepositoryPort {

    Notification save(Notification notification);

    Optional<Notification> findById(Long id);

    Page<Notification> findByUserId(Long userId, Pageable pageable);

    long countUnreadByUserId(Long userId);

    void markAsRead(Long notificationId);

    void markAllAsReadByUserId(Long userId);
}

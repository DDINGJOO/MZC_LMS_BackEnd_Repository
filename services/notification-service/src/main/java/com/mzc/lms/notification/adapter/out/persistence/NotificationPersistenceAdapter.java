package com.mzc.lms.notification.adapter.out.persistence;

import com.mzc.lms.notification.adapter.out.persistence.entity.NotificationEntity;
import com.mzc.lms.notification.adapter.out.persistence.repository.NotificationJpaRepository;
import com.mzc.lms.notification.application.port.out.NotificationRepositoryPort;
import com.mzc.lms.notification.domain.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements NotificationRepositoryPort {

    private final NotificationJpaRepository repository;

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = toEntity(notification);
        NotificationEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Page<Notification> findByUserId(Long userId, Pageable pageable) {
        return repository.findByUserId(userId, pageable).map(this::toDomain);
    }

    @Override
    public long countUnreadByUserId(Long userId) {
        return repository.countUnreadByUserId(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        repository.markAsRead(notificationId);
    }

    @Override
    public void markAllAsReadByUserId(Long userId) {
        repository.markAllAsReadByUserId(userId);
    }

    private Notification toDomain(NotificationEntity entity) {
        return Notification.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .type(entity.getType())
                .channel(entity.getChannel())
                .title(entity.getTitle())
                .content(entity.getContent())
                .data(entity.getData())
                .isRead(entity.getIsRead())
                .createdAt(entity.getCreatedAt())
                .readAt(entity.getReadAt())
                .build();
    }

    private NotificationEntity toEntity(Notification domain) {
        return NotificationEntity.builder()
                .userId(domain.getUserId())
                .type(domain.getType())
                .channel(domain.getChannel())
                .title(domain.getTitle())
                .content(domain.getContent())
                .data(domain.getData())
                .build();
    }
}

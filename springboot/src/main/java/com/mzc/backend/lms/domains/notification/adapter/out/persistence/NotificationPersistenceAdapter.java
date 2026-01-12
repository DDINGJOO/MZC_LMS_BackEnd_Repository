package com.mzc.backend.lms.domains.notification.adapter.out.persistence;

import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.Notification;
import com.mzc.backend.lms.domains.notification.adapter.out.persistence.repository.NotificationRepository;
import com.mzc.backend.lms.domains.notification.application.port.out.NotificationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 알림 Persistence Adapter
 */
@Component
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements NotificationRepositoryPort {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> saveAll(List<Notification> notifications) {
        return notificationRepository.saveAll(notifications);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public List<Notification> findByRecipientIdOrderByIdDesc(Long recipientId, Pageable pageable) {
        return notificationRepository.findByRecipientIdOrderByIdDesc(recipientId, pageable);
    }

    @Override
    public List<Notification> findByRecipientIdAndIdLessThanOrderByIdDesc(Long recipientId, Long cursor, Pageable pageable) {
        return notificationRepository.findByRecipientIdAndIdLessThanOrderByIdDesc(recipientId, cursor, pageable);
    }

    @Override
    public List<Notification> findUnreadByRecipientIdOrderByIdDesc(Long recipientId, Pageable pageable) {
        return notificationRepository.findUnreadByRecipientIdOrderByIdDesc(recipientId, pageable);
    }

    @Override
    public List<Notification> findUnreadByRecipientIdAndIdLessThanOrderByIdDesc(Long recipientId, Long cursor, Pageable pageable) {
        return notificationRepository.findUnreadByRecipientIdAndIdLessThanOrderByIdDesc(recipientId, cursor, pageable);
    }

    @Override
    public long countUnreadByRecipientId(Long recipientId) {
        return notificationRepository.countUnreadByRecipientId(recipientId);
    }

    @Override
    public int markAllAsReadByRecipientId(Long recipientId) {
        return notificationRepository.markAllAsReadByRecipientId(recipientId);
    }

    @Override
    public void deleteById(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public int deleteReadByRecipientId(Long recipientId) {
        return notificationRepository.deleteReadByRecipientId(recipientId);
    }

    @Override
    public int deleteAllByRecipientId(Long recipientId) {
        return notificationRepository.deleteAllByRecipientId(recipientId);
    }
}

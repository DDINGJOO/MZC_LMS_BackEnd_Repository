package com.mzc.backend.lms.domains.notification.adapter.out.persistence;

import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.NotificationBatch;
import com.mzc.backend.lms.domains.notification.adapter.out.persistence.repository.NotificationBatchRepository;
import com.mzc.backend.lms.domains.notification.application.port.out.NotificationBatchRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 대량 알림 배치 Persistence Adapter
 */
@Component
@RequiredArgsConstructor
public class NotificationBatchPersistenceAdapter implements NotificationBatchRepositoryPort {

    private final NotificationBatchRepository notificationBatchRepository;

    @Override
    public NotificationBatch save(NotificationBatch batch) {
        return notificationBatchRepository.save(batch);
    }

    @Override
    public Optional<NotificationBatch> findById(Long id) {
        return notificationBatchRepository.findById(id);
    }
}

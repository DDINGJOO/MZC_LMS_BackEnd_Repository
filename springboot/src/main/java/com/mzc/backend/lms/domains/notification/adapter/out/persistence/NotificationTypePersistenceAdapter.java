package com.mzc.backend.lms.domains.notification.adapter.out.persistence;

import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.NotificationType;
import com.mzc.backend.lms.domains.notification.adapter.out.persistence.repository.NotificationTypeRepository;
import com.mzc.backend.lms.domains.notification.application.port.out.NotificationTypeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 알림 타입 Persistence Adapter
 */
@Component
@RequiredArgsConstructor
public class NotificationTypePersistenceAdapter implements NotificationTypeRepositoryPort {

    private final NotificationTypeRepository notificationTypeRepository;

    @Override
    public Optional<NotificationType> findByTypeCode(String typeCode) {
        return notificationTypeRepository.findByTypeCode(typeCode);
    }

    @Override
    public Optional<NotificationType> findById(Integer id) {
        return notificationTypeRepository.findById(id);
    }
}

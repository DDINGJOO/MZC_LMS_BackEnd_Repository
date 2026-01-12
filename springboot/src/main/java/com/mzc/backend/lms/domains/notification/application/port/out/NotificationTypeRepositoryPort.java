package com.mzc.backend.lms.domains.notification.application.port.out;

import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.NotificationType;

import java.util.Optional;

/**
 * 알림 타입 Repository Port
 */
public interface NotificationTypeRepositoryPort {

    /**
     * 타입 코드로 알림 타입 조회
     */
    Optional<NotificationType> findByTypeCode(String typeCode);

    /**
     * ID로 알림 타입 조회
     */
    Optional<NotificationType> findById(Integer id);
}

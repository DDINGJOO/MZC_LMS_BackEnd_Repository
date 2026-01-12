package com.mzc.backend.lms.domains.notification.application.port.out;

import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.NotificationBatch;

import java.util.Optional;

/**
 * 대량 알림 배치 Repository Port (Outbound Port)
 */
public interface NotificationBatchRepositoryPort {

    /**
     * 배치 저장
     */
    NotificationBatch save(NotificationBatch batch);

    /**
     * ID로 배치 조회
     */
    Optional<NotificationBatch> findById(Long id);
}

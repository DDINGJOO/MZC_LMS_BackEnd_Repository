package com.mzc.backend.lms.domains.notification.application.port.out;

import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.NotificationPreference;

import java.util.Optional;

/**
 * 알림 설정 Port
 */
public interface NotificationPreferencePort {

    /**
     * 사용자 ID로 알림 설정 조회
     */
    Optional<NotificationPreference> findByUserId(Long userId);

    /**
     * 알림 설정 저장
     */
    NotificationPreference save(NotificationPreference preference);
}

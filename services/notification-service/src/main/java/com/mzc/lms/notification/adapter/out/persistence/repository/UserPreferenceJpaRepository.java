package com.mzc.lms.notification.adapter.out.persistence.repository;

import com.mzc.lms.notification.adapter.out.persistence.entity.UserNotificationPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPreferenceJpaRepository extends JpaRepository<UserNotificationPreferenceEntity, Long> {

    Optional<UserNotificationPreferenceEntity> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}

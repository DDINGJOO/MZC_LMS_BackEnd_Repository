package com.mzc.lms.notification.application.port.out;

import com.mzc.lms.notification.domain.model.UserNotificationPreference;

import java.util.Optional;

public interface UserPreferenceRepositoryPort {

    UserNotificationPreference save(UserNotificationPreference preference);

    Optional<UserNotificationPreference> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}

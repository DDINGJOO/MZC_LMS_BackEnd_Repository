package com.mzc.lms.notification.adapter.out.persistence;

import com.mzc.lms.notification.adapter.out.persistence.entity.UserNotificationPreferenceEntity;
import com.mzc.lms.notification.adapter.out.persistence.repository.UserPreferenceJpaRepository;
import com.mzc.lms.notification.application.port.out.UserPreferenceRepositoryPort;
import com.mzc.lms.notification.domain.model.UserNotificationPreference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPreferencePersistenceAdapter implements UserPreferenceRepositoryPort {

    private final UserPreferenceJpaRepository repository;

    @Override
    public UserNotificationPreference save(UserNotificationPreference preference) {
        UserNotificationPreferenceEntity entity = preference.getId() != null
                ? repository.findById(preference.getId())
                    .map(existing -> {
                        existing.getEnabledChannels().clear();
                        existing.getEnabledChannels().addAll(preference.getEnabledChannels());
                        existing.updateQuietHours(preference.getQuietHoursStart(), preference.getQuietHoursEnd());
                        return existing;
                    })
                    .orElseGet(() -> toEntity(preference))
                : toEntity(preference);

        UserNotificationPreferenceEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<UserNotificationPreference> findByUserId(Long userId) {
        return repository.findByUserId(userId).map(this::toDomain);
    }

    @Override
    public void deleteByUserId(Long userId) {
        repository.deleteByUserId(userId);
    }

    private UserNotificationPreference toDomain(UserNotificationPreferenceEntity entity) {
        return UserNotificationPreference.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .enabledChannels(new HashSet<>(entity.getEnabledChannels()))
                .quietHoursStart(entity.getQuietHoursStart())
                .quietHoursEnd(entity.getQuietHoursEnd())
                .build();
    }

    private UserNotificationPreferenceEntity toEntity(UserNotificationPreference domain) {
        return UserNotificationPreferenceEntity.builder()
                .userId(domain.getUserId())
                .enabledChannels(new HashSet<>(domain.getEnabledChannels()))
                .quietHoursStart(domain.getQuietHoursStart())
                .quietHoursEnd(domain.getQuietHoursEnd())
                .build();
    }
}

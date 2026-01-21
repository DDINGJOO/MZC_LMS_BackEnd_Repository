package com.mzc.lms.profile.adapter.out.persistence;

import com.mzc.lms.profile.adapter.out.persistence.entity.UserProfileEntity;
import com.mzc.lms.profile.adapter.out.persistence.repository.UserProfileJpaRepository;
import com.mzc.lms.profile.application.port.out.UserProfileRepositoryPort;
import com.mzc.lms.profile.domain.model.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserProfilePersistenceAdapter implements UserProfileRepositoryPort {

    private final UserProfileJpaRepository repository;

    @Override
    public Optional<UserProfile> findByUserId(Long userId) {
        return repository.findByUserId(userId)
                .map(this::toDomain);
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        UserProfileEntity entity = repository.findByUserId(userProfile.getUserId())
                .map(existing -> {
                    existing.updateName(userProfile.getName());
                    existing.updateProfileImage(userProfile.getProfileImageUrl());
                    return existing;
                })
                .orElseGet(() -> toEntity(userProfile));

        UserProfileEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return repository.existsByUserId(userId);
    }

    private UserProfile toDomain(UserProfileEntity entity) {
        return UserProfile.builder()
                .userId(entity.getUserId())
                .name(entity.getName())
                .email(entity.getEmail())
                .userType(entity.getUserType())
                .profileImageUrl(entity.getProfileImageUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private UserProfileEntity toEntity(UserProfile domain) {
        return UserProfileEntity.builder()
                .userId(domain.getUserId())
                .name(domain.getName())
                .email(domain.getEmail())
                .userType(domain.getUserType())
                .profileImageUrl(domain.getProfileImageUrl())
                .build();
    }
}

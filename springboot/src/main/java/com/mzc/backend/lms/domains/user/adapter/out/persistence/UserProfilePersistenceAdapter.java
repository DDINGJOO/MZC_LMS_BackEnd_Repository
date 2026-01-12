package com.mzc.backend.lms.domains.user.adapter.out.persistence;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserProfile;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.UserProfileRepository;
import com.mzc.backend.lms.domains.user.application.port.out.UserProfileCommandPort;
import com.mzc.backend.lms.domains.user.application.port.out.UserProfileQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * UserProfile 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
public class UserProfilePersistenceAdapter implements UserProfileQueryPort, UserProfileCommandPort {

    private final UserProfileRepository userProfileRepository;

    @Override
    public Optional<UserProfile> findByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId);
    }

    @Override
    public List<UserProfile> findByUserIds(List<Long> userIds) {
        return userProfileRepository.findByUserIds(userIds);
    }

    @Override
    public List<Object[]> findNamesByUserIds(List<Long> userIds) {
        return userProfileRepository.findNamesByUserIds(userIds);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return userProfileRepository.existsByUserId(userId);
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }
}

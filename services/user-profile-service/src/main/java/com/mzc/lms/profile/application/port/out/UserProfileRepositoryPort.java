package com.mzc.lms.profile.application.port.out;

import com.mzc.lms.profile.domain.model.UserProfile;

import java.util.Optional;

public interface UserProfileRepositoryPort {

    Optional<UserProfile> findByUserId(Long userId);

    UserProfile save(UserProfile userProfile);

    boolean existsByUserId(Long userId);
}

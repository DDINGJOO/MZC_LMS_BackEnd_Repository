package com.mzc.backend.lms.domains.user.adapter.out.persistence;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserProfileImage;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.UserProfileImageRepository;
import com.mzc.backend.lms.domains.user.application.port.out.UserProfileImageCommandPort;
import com.mzc.backend.lms.domains.user.application.port.out.UserProfileImageQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * UserProfileImage 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
public class UserProfileImagePersistenceAdapter implements UserProfileImageQueryPort, UserProfileImageCommandPort {

    private final UserProfileImageRepository userProfileImageRepository;

    @Override
    public Optional<UserProfileImage> findByUserId(Long userId) {
        return userProfileImageRepository.findByUserId(userId);
    }

    @Override
    public List<UserProfileImage> findByUserIds(List<Long> userIds) {
        return userProfileImageRepository.findByUserIds(userIds);
    }

    @Override
    public List<Long> findUserIdsWithProfileImage(List<Long> userIds) {
        return userProfileImageRepository.findUserIdsWithProfileImage(userIds);
    }

    @Override
    public UserProfileImage save(UserProfileImage image) {
        return userProfileImageRepository.save(image);
    }

    @Override
    public int deleteByUserId(Long userId) {
        return userProfileImageRepository.deleteByUserIdQuery(userId);
    }
}

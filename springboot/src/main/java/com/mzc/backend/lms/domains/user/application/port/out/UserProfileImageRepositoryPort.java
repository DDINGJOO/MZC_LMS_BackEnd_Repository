package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserProfileImage;

import java.util.Optional;

/**
 * UserProfileImage 영속성 Port
 * Auth 도메인에서 UserProfileImage 엔티티 접근을 위한 인터페이스
 */
public interface UserProfileImageRepositoryPort {

    /**
     * 사용자 ID로 프로필 이미지 조회
     */
    Optional<UserProfileImage> findByUserId(Long userId);
}

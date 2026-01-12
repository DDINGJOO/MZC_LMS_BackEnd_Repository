package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserProfile;

import java.util.Optional;

/**
 * UserProfile 영속성 Port
 * Auth 도메인에서 UserProfile 엔티티 접근을 위한 인터페이스
 */
public interface UserProfileRepositoryPort {

    /**
     * 프로필 저장
     */
    UserProfile save(UserProfile userProfile);

    /**
     * 사용자 ID로 프로필 조회
     */
    Optional<UserProfile> findByUserId(Long userId);
}

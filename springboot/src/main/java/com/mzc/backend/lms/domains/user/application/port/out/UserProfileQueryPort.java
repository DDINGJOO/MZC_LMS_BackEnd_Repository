package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserProfile;

import java.util.List;
import java.util.Optional;

/**
 * UserProfile 조회 Port
 * User 도메인에서 UserProfile 엔티티 조회를 위한 인터페이스
 */
public interface UserProfileQueryPort {

    /**
     * 사용자 ID로 프로필 조회
     */
    Optional<UserProfile> findByUserId(Long userId);

    /**
     * 여러 User ID로 프로필 일괄 조회
     */
    List<UserProfile> findByUserIds(List<Long> userIds);

    /**
     * 여러 User ID의 이름만 조회 (프로젝션)
     */
    List<Object[]> findNamesByUserIds(List<Long> userIds);

    /**
     * User ID 존재 여부 확인
     */
    boolean existsByUserId(Long userId);
}

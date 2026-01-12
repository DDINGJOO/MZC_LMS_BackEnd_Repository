package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserProfileImage;

import java.util.List;
import java.util.Optional;

/**
 * UserProfileImage 조회 Port
 * User 도메인에서 UserProfileImage 엔티티 조회를 위한 인터페이스
 */
public interface UserProfileImageQueryPort {

    /**
     * 사용자 ID로 프로필 이미지 조회
     */
    Optional<UserProfileImage> findByUserId(Long userId);

    /**
     * 여러 사용자의 프로필 이미지 일괄 조회
     */
    List<UserProfileImage> findByUserIds(List<Long> userIds);

    /**
     * 프로필 이미지가 있는 사용자 ID 목록 조회
     */
    List<Long> findUserIdsWithProfileImage(List<Long> userIds);
}

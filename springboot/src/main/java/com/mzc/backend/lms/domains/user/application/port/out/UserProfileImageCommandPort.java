package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserProfileImage;

/**
 * UserProfileImage 명령 Port
 * User 도메인에서 UserProfileImage 엔티티 저장/수정/삭제를 위한 인터페이스
 */
public interface UserProfileImageCommandPort {

    /**
     * 프로필 이미지 저장
     */
    UserProfileImage save(UserProfileImage image);

    /**
     * 사용자 ID로 프로필 이미지 삭제
     */
    int deleteByUserId(Long userId);
}

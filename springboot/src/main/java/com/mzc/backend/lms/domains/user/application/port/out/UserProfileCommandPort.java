package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserProfile;

/**
 * UserProfile 명령 Port
 * User 도메인에서 UserProfile 엔티티 저장/수정을 위한 인터페이스
 */
public interface UserProfileCommandPort {

    /**
     * 프로필 저장
     */
    UserProfile save(UserProfile userProfile);
}

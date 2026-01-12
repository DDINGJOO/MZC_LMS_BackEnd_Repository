package com.mzc.backend.lms.domains.notification.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.User;

import java.util.Optional;

/**
 * 사용자 조회 Port (외부 도메인 의존)
 */
public interface UserLookupPort {

    /**
     * ID로 사용자 조회
     */
    Optional<User> findById(Long userId);
}

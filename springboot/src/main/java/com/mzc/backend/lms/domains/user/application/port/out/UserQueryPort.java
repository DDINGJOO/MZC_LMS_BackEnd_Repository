package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.User;

import java.util.Optional;

/**
 * User 조회 Port
 * User 도메인에서 User 엔티티 조회를 위한 인터페이스
 */
public interface UserQueryPort {

    /**
     * ID로 사용자 조회
     */
    Optional<User> findById(Long id);

    /**
     * 이메일로 사용자 조회
     */
    Optional<User> findByEmail(String email);

    /**
     * 이메일 존재 여부 확인
     */
    boolean existsByEmail(String email);
}

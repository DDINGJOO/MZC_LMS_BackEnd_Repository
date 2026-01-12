package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.User;

import java.util.Optional;

/**
 * User 영속성 Port
 * Auth 도메인에서 User 엔티티 접근을 위한 인터페이스
 */
public interface UserRepositoryPort {

    /**
     * 사용자 저장
     */
    User save(User user);

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

    /**
     * 특정 prefix로 시작하는 학번의 최대 순번 조회
     */
    Optional<Integer> findMaxSequenceByPrefix(String prefix);
}

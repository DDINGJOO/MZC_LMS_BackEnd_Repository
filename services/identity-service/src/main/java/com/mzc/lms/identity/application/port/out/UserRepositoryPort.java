package com.mzc.lms.identity.application.port.out;

import com.mzc.lms.identity.domain.model.User;

import java.util.Optional;

/**
 * 사용자 Repository Port
 */
public interface UserRepositoryPort {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);
}

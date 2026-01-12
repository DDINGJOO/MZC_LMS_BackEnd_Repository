package com.mzc.backend.lms.domains.user.adapter.out.persistence;

import com.mzc.backend.lms.domains.user.application.port.out.UserQueryPort;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.User;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * User 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserQueryPort {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}

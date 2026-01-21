package com.mzc.lms.identity.adapter.out.persistence;

import com.mzc.lms.identity.adapter.out.persistence.entity.UserEntity;
import com.mzc.lms.identity.adapter.out.persistence.repository.UserJpaRepository;
import com.mzc.lms.identity.application.port.out.UserRepositoryPort;
import com.mzc.lms.identity.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(this::toDomain);
    }

    private User toDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .userType(entity.getUserType())
                .userNumber(entity.getUserNumber())
                .name(entity.getName())
                .enabled(entity.isEnabled())
                .build();
    }
}

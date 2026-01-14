package com.mzc.backend.lms.domains.board.adapter.out.persistence;

import com.mzc.backend.lms.domains.board.adapter.out.persistence.repository.UserTypeQueryRepositoryJpa;
import com.mzc.backend.lms.domains.board.application.port.out.UserTypeQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 사용자 타입 조회 Adapter
 */
@Component
@RequiredArgsConstructor
public class UserTypeQueryAdapter implements UserTypeQueryPort {

    private final UserTypeQueryRepositoryJpa userTypeQueryRepositoryJpa;

    @Override
    public Optional<String> findUserTypeCodeByUserId(Long userId) {
        return userTypeQueryRepositoryJpa.findUserTypeCodeByUserId(userId);
    }

    @Override
    public boolean isStudent(Long userId) {
        return userTypeQueryRepositoryJpa.isStudent(userId);
    }

    @Override
    public boolean isProfessor(Long userId) {
        return userTypeQueryRepositoryJpa.isProfessor(userId);
    }
}

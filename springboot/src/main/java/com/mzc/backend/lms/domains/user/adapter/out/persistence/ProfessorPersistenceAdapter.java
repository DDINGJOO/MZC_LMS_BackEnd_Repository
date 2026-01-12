package com.mzc.backend.lms.domains.user.adapter.out.persistence;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Professor;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.ProfessorRepository;
import com.mzc.backend.lms.domains.user.application.port.out.ProfessorQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Professor 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
public class ProfessorPersistenceAdapter implements ProfessorQueryPort {

    private final ProfessorRepository professorRepository;

    @Override
    public Optional<Professor> findById(Long professorId) {
        return professorRepository.findById(professorId);
    }

    @Override
    public boolean existsById(Long professorId) {
        return professorRepository.existsById(professorId);
    }

    @Override
    public List<Professor> findAllById(List<Long> professorIds) {
        return professorRepository.findAllById(professorIds);
    }
}

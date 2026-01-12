package com.mzc.backend.lms.domains.user.adapter.out.persistence;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.ProfessorDepartment;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.ProfessorDepartmentRepository;
import com.mzc.backend.lms.domains.user.application.port.out.ProfessorDepartmentQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * ProfessorDepartment 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
public class ProfessorDepartmentPersistenceAdapter implements ProfessorDepartmentQueryPort {

    private final ProfessorDepartmentRepository professorDepartmentRepository;

    @Override
    public Optional<ProfessorDepartment> findByProfessorId(Long professorId) {
        return professorDepartmentRepository.findByProfessorId(professorId);
    }
}

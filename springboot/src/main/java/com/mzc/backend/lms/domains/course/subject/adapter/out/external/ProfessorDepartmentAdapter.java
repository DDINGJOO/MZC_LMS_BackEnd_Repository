package com.mzc.backend.lms.domains.course.subject.adapter.out.external;

import com.mzc.backend.lms.domains.course.subject.application.port.out.ProfessorDepartmentPort;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.ProfessorDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 교수-학과 외부 Adapter (user/professor 도메인)
 */
@Component
@RequiredArgsConstructor
public class ProfessorDepartmentAdapter implements ProfessorDepartmentPort {

    private final ProfessorDepartmentRepository professorDepartmentRepository;

    @Override
    public Optional<Long> findDepartmentIdByProfessorId(Long professorId) {
        return professorDepartmentRepository.findByProfessorId(professorId)
                .map(pd -> pd.getDepartment().getId());
    }
}

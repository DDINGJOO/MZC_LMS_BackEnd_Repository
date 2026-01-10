package com.mzc.backend.lms.domains.course.course.adapter.out.external;

import com.mzc.backend.lms.domains.course.course.application.port.out.ProfessorPort;
import com.mzc.backend.lms.domains.user.organization.entity.Department;
import com.mzc.backend.lms.domains.user.professor.entity.Professor;
import com.mzc.backend.lms.domains.user.professor.repository.ProfessorRepository;
import com.mzc.backend.lms.domains.user.professor.repository.ProfessorDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 교수 외부 Adapter (user 도메인)
 */
@Component("courseProfessorAdapter")
@RequiredArgsConstructor
public class ProfessorAdapter implements ProfessorPort {

    private final ProfessorRepository professorRepository;
    private final ProfessorDepartmentRepository professorDepartmentRepository;

    @Override
    public Optional<Professor> findById(Long professorId) {
        return professorRepository.findById(professorId);
    }

    @Override
    public Optional<Department> findPrimaryDepartmentByProfessorId(Long professorId) {
        return professorDepartmentRepository.findByProfessorId(professorId)
                .filter(pd -> pd.getIsPrimary() != null && pd.getIsPrimary())
                .map(pd -> pd.getDepartment());
    }
}

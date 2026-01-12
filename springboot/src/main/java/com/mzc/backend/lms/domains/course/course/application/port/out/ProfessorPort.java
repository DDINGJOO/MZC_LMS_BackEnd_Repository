package com.mzc.backend.lms.domains.course.course.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Department;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Professor;

import java.util.Optional;

/**
 * 교수 외부 Port (user 도메인)
 */
public interface ProfessorPort {

    /**
     * ID로 교수 조회
     */
    Optional<Professor> findById(Long professorId);

    /**
     * 교수의 주 소속 학과 조회
     */
    Optional<Department> findPrimaryDepartmentByProfessorId(Long professorId);
}

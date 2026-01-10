package com.mzc.backend.lms.domains.course.subject.application.port.out;

import java.util.Optional;

/**
 * 교수-학과 외부 Port (user/professor 도메인)
 */
public interface ProfessorDepartmentPort {

    /**
     * 교수 ID로 소속 학과 ID 조회
     */
    Optional<Long> findDepartmentIdByProfessorId(Long professorId);
}

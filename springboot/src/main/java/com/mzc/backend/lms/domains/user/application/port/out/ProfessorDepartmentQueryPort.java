package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.ProfessorDepartment;

import java.util.Optional;

/**
 * ProfessorDepartment 조회 Port
 * User 도메인에서 ProfessorDepartment 엔티티 조회를 위한 인터페이스
 */
public interface ProfessorDepartmentQueryPort {

    /**
     * 교수 ID로 학과 정보 조회
     */
    Optional<ProfessorDepartment> findByProfessorId(Long professorId);
}

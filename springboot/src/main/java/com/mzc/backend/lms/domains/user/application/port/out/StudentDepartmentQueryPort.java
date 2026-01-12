package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.StudentDepartment;

import java.util.Optional;

/**
 * StudentDepartment 조회 Port
 * User 도메인에서 StudentDepartment 엔티티 조회를 위한 인터페이스
 */
public interface StudentDepartmentQueryPort {

    /**
     * 학생 ID로 학과 정보 조회
     */
    Optional<StudentDepartment> findByStudentId(Long studentId);
}

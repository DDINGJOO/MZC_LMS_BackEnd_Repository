package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.StudentDepartment;

import java.util.Optional;

/**
 * StudentDepartment 영속성 Port
 * Auth 도메인에서 StudentDepartment 엔티티 접근을 위한 인터페이스
 */
public interface StudentDepartmentRepositoryPort {

    /**
     * 학생 학과 저장
     */
    StudentDepartment save(StudentDepartment studentDepartment);

    /**
     * 학번으로 학생 학과 조회
     */
    Optional<StudentDepartment> findByStudentId(Long studentId);
}

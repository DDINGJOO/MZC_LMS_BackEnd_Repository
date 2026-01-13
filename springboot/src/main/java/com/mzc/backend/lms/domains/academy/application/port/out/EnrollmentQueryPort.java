package com.mzc.backend.lms.domains.academy.application.port.out;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;

import java.util.List;

/**
 * Enrollment 도메인 조회 Port (외부 도메인 연동)
 */
public interface EnrollmentQueryPort {

    /**
     * 학생의 수강 학기 목록 조회
     */
    List<AcademicTerm> findDistinctAcademicTermsByStudentId(Long studentId);
}

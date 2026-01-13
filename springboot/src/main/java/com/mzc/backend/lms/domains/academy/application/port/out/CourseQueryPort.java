package com.mzc.backend.lms.domains.academy.application.port.out;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;

import java.util.List;

/**
 * Course 도메인 조회 Port (외부 도메인 연동)
 */
public interface CourseQueryPort {

    /**
     * 교수의 담당 학기 목록 조회
     */
    List<AcademicTerm> findDistinctAcademicTermsByProfessorId(Long professorId);
}

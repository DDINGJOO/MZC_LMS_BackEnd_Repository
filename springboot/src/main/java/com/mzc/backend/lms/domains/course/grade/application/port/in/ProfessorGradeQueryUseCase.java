package com.mzc.backend.lms.domains.course.grade.application.port.in;

import com.mzc.backend.lms.domains.course.grade.adapter.in.web.dto.ProfessorCourseGradesResponseDto;

import java.util.List;

/**
 * 교수 성적 조회 UseCase
 */
public interface ProfessorGradeQueryUseCase {

    /**
     * 강의 성적 목록 조회
     */
    List<ProfessorCourseGradesResponseDto> listCourseGrades(Long courseId, Long professorId, GradeQueryStatus status);

    /**
     * 성적 조회 상태
     */
    enum GradeQueryStatus {
        ALL, PUBLISHED
    }
}

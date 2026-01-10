package com.mzc.backend.lms.domains.course.grade.application.port.in;

import com.mzc.backend.lms.domains.course.grade.adapter.in.web.dto.StudentGradeResponseDto;

import java.util.List;

/**
 * 학생 성적 조회 UseCase
 */
public interface StudentGradeUseCase {

    /**
     * 공개된 성적 목록 조회
     */
    List<StudentGradeResponseDto> listPublishedGrades(Long studentId, Long academicTermId);
}

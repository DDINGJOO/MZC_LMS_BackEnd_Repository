package com.mzc.backend.lms.domains.course.course.application.port.in;

import com.mzc.backend.lms.domains.course.course.adapter.in.web.dto.*;

/**
 * 교수 강의 관리 UseCase
 */
public interface ProfessorCourseUseCase {

    /**
     * 강의 개설
     */
    CreateCourseResponseDto createCourse(CreateCourseRequestDto request, Long professorId);

    /**
     * 강의 수정
     */
    CreateCourseResponseDto updateCourse(Long courseId, UpdateCourseRequestDto request, Long professorId);

    /**
     * 강의 취소
     */
    void cancelCourse(Long courseId, Long professorId);

    /**
     * 내가 개설한 강의 목록 조회
     */
    MyCoursesResponseDto getMyCourses(Long professorId, Long academicTermId);

    /**
     * 교수 강의 상세 조회
     */
    ProfessorCourseDetailDto getCourseDetail(Long courseId, Long professorId);
}

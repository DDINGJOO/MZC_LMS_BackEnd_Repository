package com.mzc.backend.lms.domains.course.course.application.port.in;

import com.mzc.backend.lms.domains.course.course.adapter.in.web.dto.CourseDetailDto;
import com.mzc.backend.lms.domains.course.course.adapter.in.web.dto.CourseDto;
import com.mzc.backend.lms.domains.course.course.adapter.in.web.dto.CourseResponseDto;
import com.mzc.backend.lms.domains.course.course.adapter.in.web.dto.CourseSearchRequestDto;

/**
 * 강의 조회 UseCase
 */
public interface CourseQueryUseCase {

    /**
     * 강의 검색
     */
    CourseResponseDto searchCourses(CourseSearchRequestDto request);

    /**
     * 강의 상세 조회
     */
    CourseDetailDto getCourseDetail(Long courseId);

    /**
     * Course 엔티티를 CourseDto로 변환
     */
    CourseDto convertToCourseDto(com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.Course course);
}

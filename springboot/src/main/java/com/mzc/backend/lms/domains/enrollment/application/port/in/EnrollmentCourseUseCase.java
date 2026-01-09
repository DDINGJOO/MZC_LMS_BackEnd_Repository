package com.mzc.backend.lms.domains.enrollment.application.port.in;

import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request.CourseSearchRequestDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.CourseListResponseDto;

/**
 * 수강신청 강의 조회 UseCase (Inbound Port)
 */
public interface EnrollmentCourseUseCase {

    /**
     * 강의 목록 조회 (검색 및 필터링)
     */
    CourseListResponseDto searchCourses(CourseSearchRequestDto request, String studentId);
}

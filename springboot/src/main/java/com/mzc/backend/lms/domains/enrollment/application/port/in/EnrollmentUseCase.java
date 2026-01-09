package com.mzc.backend.lms.domains.enrollment.application.port.in;

import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request.CourseIdsRequestDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request.EnrollmentBulkCancelRequestDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.EnrollmentBulkCancelResponseDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.EnrollmentBulkResponseDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.MyEnrollmentsResponseDto;

/**
 * 수강신청 UseCase (Inbound Port)
 * Controller에서 이 인터페이스를 호출
 */
public interface EnrollmentUseCase {

    /**
     * 일괄 수강신청
     */
    EnrollmentBulkResponseDto enrollBulk(CourseIdsRequestDto request, Long studentId);

    /**
     * 내 수강신청 목록 조회
     */
    MyEnrollmentsResponseDto getMyEnrollments(Long studentId, Long enrollmentPeriodId);

    /**
     * 일괄 수강신청 취소
     */
    EnrollmentBulkCancelResponseDto cancelBulk(EnrollmentBulkCancelRequestDto request, Long studentId);
}

package com.mzc.backend.lms.domains.enrollment.adapter.in.web;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request.CourseIdsRequestDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.CourseListResponseDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request.CourseSearchRequestDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request.EnrollmentBulkCancelRequestDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.EnrollmentBulkCancelResponseDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.EnrollmentBulkResponseDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.EnrollmentPeriodResponseDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.MyEnrollmentsResponseDto;
import com.mzc.backend.lms.domains.enrollment.application.port.in.EnrollmentCourseUseCase;
import com.mzc.backend.lms.domains.enrollment.application.port.in.EnrollmentPeriodUseCase;
import com.mzc.backend.lms.domains.enrollment.application.port.in.EnrollmentUseCase;
import com.mzc.backend.lms.domains.user.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 수강신청 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentCourseUseCase enrollmentCourseUseCase;
    private final EnrollmentPeriodUseCase enrollmentPeriodUseCase;
    private final EnrollmentUseCase enrollmentUseCase;

    /**
     * 현재 활성화된 기간 조회
     */
    @GetMapping("/periods/current")
    public ResponseEntity<ApiResponse<EnrollmentPeriodResponseDto>> getCurrentPeriod(
            @RequestParam(required = false) String type) {
        EnrollmentPeriodResponseDto response = enrollmentPeriodUseCase.getCurrentPeriod(type);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 강의 목록 조회 (검색 및 필터링)
     */
    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<CourseListResponseDto>> searchCourses(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Integer courseType,
            @RequestParam(required = false) Integer credits,
            @RequestParam(required = true) Long enrollmentPeriodId,
            @RequestParam(required = false) String sort,
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("검색 파라미터: keyword={}, departmentId={}, courseType={}, credits={}, enrollmentPeriodId={}",
                keyword, departmentId, courseType, credits, enrollmentPeriodId);

        CourseSearchRequestDto request = CourseSearchRequestDto.builder()
                .page(page)
                .size(size)
                .keyword(keyword)
                .departmentId(departmentId)
                .courseType(courseType)
                .credits(credits)
                .enrollmentPeriodId(enrollmentPeriodId)
                .sort(sort)
                .build();

        CourseListResponseDto response = enrollmentCourseUseCase.searchCourses(request, String.valueOf(studentId));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 일괄 수강신청
     */
    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<EnrollmentBulkResponseDto>> enrollBulk(
            @RequestBody CourseIdsRequestDto request,
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("일괄 수강신청: studentId={}, courseIds={}", studentId, request.getCourseIds());

        EnrollmentBulkResponseDto response = enrollmentUseCase.enrollBulk(request, studentId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 일괄 수강신청 취소
     */
    @DeleteMapping("/bulk")
    public ResponseEntity<ApiResponse<EnrollmentBulkCancelResponseDto>> cancelBulk(
            @RequestBody EnrollmentBulkCancelRequestDto request,
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("일괄 수강신청 취소: studentId={}, enrollmentIds={}", studentId, request.getEnrollmentIds());

        EnrollmentBulkCancelResponseDto response = enrollmentUseCase.cancelBulk(request, studentId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 내 수강신청 목록 조회
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<MyEnrollmentsResponseDto>> getMyEnrollments(
            @RequestParam(required = false) Long enrollmentPeriodId,
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("내 수강신청 목록 조회: studentId={}, enrollmentPeriodId={}", studentId, enrollmentPeriodId);

        MyEnrollmentsResponseDto response = enrollmentUseCase.getMyEnrollments(studentId, enrollmentPeriodId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

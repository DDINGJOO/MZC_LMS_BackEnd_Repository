package com.mzc.backend.lms.domains.dashboard.student.adapter.in.web;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.dashboard.student.adapter.in.web.dto.EnrollmentSummaryDto;
import com.mzc.backend.lms.domains.dashboard.student.adapter.in.web.dto.NoticeDto;
import com.mzc.backend.lms.domains.dashboard.student.adapter.in.web.dto.PendingAssignmentDto;
import com.mzc.backend.lms.domains.dashboard.student.adapter.in.web.dto.TodayCourseDto;
import com.mzc.backend.lms.domains.dashboard.student.application.service.StudentDashboardService;
import com.mzc.backend.lms.domains.user.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 학생 대시보드 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/dashboard/student")
@RequiredArgsConstructor
public class StudentDashboardController {

    private static final int MAX_DAYS = 30;

    private final StudentDashboardService studentDashboardService;

    /**
     * 미제출 과제 목록 조회
     *
     * @param studentId 학생 ID (JWT에서 추출)
     * @param days 마감일 기준 일수 (기본값: 7, 최대: 30)
     * @return 미제출 과제 목록
     */
    @GetMapping("/pending-assignments")
    public ResponseEntity<ApiResponse<List<PendingAssignmentDto>>> getPendingAssignments(
            @AuthenticationPrincipal Long studentId,
            @RequestParam(defaultValue = "7") int days) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }
        int validDays = validateDays(days);
        List<PendingAssignmentDto> assignments =
                studentDashboardService.getPendingAssignments(studentId, validDays);
        return ResponseEntity.ok(ApiResponse.success(assignments));
    }

    /**
     * 오늘의 강의 목록 조회
     *
     * @param studentId 학생 ID (JWT에서 추출)
     * @return 오늘의 강의 목록
     */
    @GetMapping("/today-courses")
    public ResponseEntity<ApiResponse<List<TodayCourseDto>>> getTodayCourses(
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }
        List<TodayCourseDto> courses = studentDashboardService.getTodayCourses(studentId);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    /**
     * 최신 공지사항 목록 조회
     *
     * @param limit 조회할 개수 (기본값: 5, 최대: 10)
     * @return 최신 공지사항 목록
     */
    @GetMapping("/notices")
    public ResponseEntity<ApiResponse<List<NoticeDto>>> getLatestNotices(
            @RequestParam(defaultValue = "5") int limit) {
        int validLimit = Math.min(Math.max(limit, 1), 10);
        List<NoticeDto> notices = studentDashboardService.getLatestNotices(validLimit);
        return ResponseEntity.ok(ApiResponse.success(notices));
    }

    /**
     * 수강 현황 요약 조회
     *
     * @param studentId 학생 ID (JWT에서 추출)
     * @return 수강 중인 과목 수와 총 학점
     */
    @GetMapping("/enrollment-summary")
    public ResponseEntity<ApiResponse<EnrollmentSummaryDto>> getEnrollmentSummary(
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }
        EnrollmentSummaryDto summary = studentDashboardService.getEnrollmentSummary(studentId);
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    private int validateDays(int days) {
        return Math.min(Math.max(days, 1), MAX_DAYS);
    }
}

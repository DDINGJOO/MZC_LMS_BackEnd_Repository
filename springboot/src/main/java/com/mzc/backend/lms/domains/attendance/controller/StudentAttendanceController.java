package com.mzc.backend.lms.domains.attendance.controller;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.attendance.dto.CourseAttendanceDto;
import com.mzc.backend.lms.domains.attendance.dto.CourseAttendanceSummaryDto;
import com.mzc.backend.lms.domains.attendance.dto.WeekAttendanceDto;
import com.mzc.backend.lms.domains.attendance.exception.AttendanceException;
import com.mzc.backend.lms.domains.attendance.service.AttendanceService;
import com.mzc.backend.lms.domains.user.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 학생용 출석 조회 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class StudentAttendanceController {

    private final AttendanceService attendanceService;

    /**
     * 내 전체 출석 현황 조회
     * GET /api/v1/attendance/my
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<CourseAttendanceSummaryDto>>> getMyAttendance(
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }

        List<CourseAttendanceSummaryDto> response = attendanceService.getStudentAllAttendance(studentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 특정 강의 출석 현황 조회
     * GET /api/v1/attendance/courses/{courseId}
     */
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<ApiResponse<CourseAttendanceDto>> getCourseAttendance(
            @PathVariable Long courseId,
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }

        CourseAttendanceDto response = attendanceService.getStudentCourseAttendance(studentId, courseId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 주차별 출석 상세 조회
     * GET /api/v1/attendance/courses/{courseId}/weeks/{weekId}
     */
    @GetMapping("/courses/{courseId}/weeks/{weekId}")
    public ResponseEntity<ApiResponse<WeekAttendanceDto>> getWeekAttendanceDetail(
            @PathVariable Long courseId,
            @PathVariable Long weekId,
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }

        CourseAttendanceDto courseAttendance = attendanceService.getStudentCourseAttendance(studentId, courseId);
        WeekAttendanceDto weekAttendance = courseAttendance.getWeekAttendances().stream()
                .filter(w -> w.getWeekId().equals(weekId))
                .findFirst()
                .orElseThrow(() -> AttendanceException.weekNotFound(weekId));

        return ResponseEntity.ok(ApiResponse.success(weekAttendance));
    }
}

package com.mzc.backend.lms.domains.attendance.controller;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.attendance.dto.CourseAttendanceOverviewDto;
import com.mzc.backend.lms.domains.attendance.dto.StudentAttendanceDto;
import com.mzc.backend.lms.domains.attendance.dto.WeekStudentAttendanceDto;
import com.mzc.backend.lms.domains.attendance.service.AttendanceService;
import com.mzc.backend.lms.domains.user.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 교수용 출석 관리 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/professor/courses/{courseId}")
@RequiredArgsConstructor
public class ProfessorAttendanceController {

    private final AttendanceService attendanceService;

    /**
     * 강의 전체 출석 현황 조회
     * GET /api/v1/professor/courses/{courseId}/attendance
     */
    @GetMapping("/attendance")
    public ResponseEntity<ApiResponse<CourseAttendanceOverviewDto>> getCourseAttendanceOverview(
            @PathVariable Long courseId,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        CourseAttendanceOverviewDto response = attendanceService.getProfessorCourseAttendance(professorId, courseId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 학생별 출석 목록 조회
     * GET /api/v1/professor/courses/{courseId}/attendance/students
     */
    @GetMapping("/attendance/students")
    public ResponseEntity<ApiResponse<List<StudentAttendanceDto>>> getStudentAttendances(
            @PathVariable Long courseId,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        List<StudentAttendanceDto> response = attendanceService.getProfessorStudentAttendances(professorId, courseId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 주차별 학생 출석 현황 조회
     * GET /api/v1/professor/courses/{courseId}/weeks/{weekId}/attendance
     */
    @GetMapping("/weeks/{weekId}/attendance")
    public ResponseEntity<ApiResponse<List<WeekStudentAttendanceDto>>> getWeekStudentAttendances(
            @PathVariable Long courseId,
            @PathVariable Long weekId,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        List<WeekStudentAttendanceDto> response = attendanceService.getProfessorWeekAttendances(professorId, courseId, weekId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

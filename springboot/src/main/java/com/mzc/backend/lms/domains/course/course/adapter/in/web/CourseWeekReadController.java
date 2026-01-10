package com.mzc.backend.lms.domains.course.course.adapter.in.web;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.course.course.adapter.in.web.dto.WeekListResponseDto;
import com.mzc.backend.lms.domains.course.course.adapter.in.web.dto.WeekContentsResponseDto;
import com.mzc.backend.lms.domains.course.course.application.service.CourseWeekContentService;
import com.mzc.backend.lms.domains.user.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 주차 목록 조회 컨트롤러 (학생/교수 공용)
 * - 수강 중 학생 또는 담당 교수만 조회 가능
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseWeekReadController {

    private final CourseWeekContentService courseWeekContentService;

    /**
     * n주차 목록 조회 (수강중 학생 / 담당 교수)
     */
    @GetMapping("/{courseId}/weeks")
    public ResponseEntity<ApiResponse<List<WeekListResponseDto>>> getWeeks(
            @PathVariable Long courseId,
            Authentication authentication
    ) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw AuthException.unauthorized();
        }

        Long requesterId = (Long) authentication.getPrincipal();
        List<WeekListResponseDto> response = courseWeekContentService.getWeeks(courseId, requesterId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 주차별 콘텐츠 목록 조회 (수강중 학생 / 담당 교수)
     */
    @GetMapping("/{courseId}/weeks/{weekId}/contents")
    public ResponseEntity<ApiResponse<WeekContentsResponseDto>> getWeekContents(
            @PathVariable Long courseId,
            @PathVariable Long weekId,
            Authentication authentication
    ) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw AuthException.unauthorized();
        }

        Long requesterId = (Long) authentication.getPrincipal();
        WeekContentsResponseDto response = courseWeekContentService.getWeekContents(courseId, weekId, requesterId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

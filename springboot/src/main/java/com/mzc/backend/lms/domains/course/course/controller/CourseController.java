package com.mzc.backend.lms.domains.course.course.controller;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.course.course.dto.CourseResponseDto;
import com.mzc.backend.lms.domains.course.course.dto.CourseSearchRequestDto;
import com.mzc.backend.lms.domains.course.course.dto.CourseDetailDto;
import com.mzc.backend.lms.domains.course.course.service.CourseService;
import com.mzc.backend.lms.domains.user.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 강의 목록 컨트롤러 (개설된 강의 조회)
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    /**
     * 강의 목록 조회 (검색 및 필터링)
     * 인증된 사용자만 접근 가능
     */
    @GetMapping
    public ResponseEntity<ApiResponse<CourseResponseDto>> searchCourses(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Integer courseType,
            @RequestParam(required = false) Integer credits,
            @RequestParam(required = true) Long enrollmentPeriodId,
            @RequestParam(required = false) String sort,
            Authentication authentication) {
        // 인증 확인
        if (authentication == null || authentication.getName() == null) {
            throw AuthException.unauthorized();
        }

        // 디버깅: 파라미터 확인
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

        CourseResponseDto response = courseService.searchCourses(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /*
    * 강의 하나에 대한 정보 상세조회
    */
    @GetMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CourseDetailDto>> getSpecificCourseInfo(
            @PathVariable Long courseId,
            Authentication authentication) {
        // 인증 확인
        if (authentication == null || authentication.getName() == null) {
            throw AuthException.unauthorized();
        }

        log.debug("강의 상세 조회: courseId={}", courseId);

        CourseDetailDto courseDetail = courseService.getCourseDetailById(courseId);
        return ResponseEntity.ok(ApiResponse.success(courseDetail));
    }
}

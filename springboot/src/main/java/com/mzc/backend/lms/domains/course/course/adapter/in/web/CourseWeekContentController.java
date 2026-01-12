package com.mzc.backend.lms.domains.course.course.adapter.in.web;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.course.course.adapter.in.web.dto.*;
import com.mzc.backend.lms.domains.course.course.application.service.CourseWeekContentService;
import com.mzc.backend.lms.domains.user.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 주차별 콘텐츠 관리 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/professor/courses/{courseId}/weeks")
@RequiredArgsConstructor
public class CourseWeekContentController {

    private final CourseWeekContentService courseWeekContentService;

    /**
     * 주차 생성
     */
    @PostMapping
    public ResponseEntity<ApiResponse<WeekDto>> createWeek(
            @PathVariable Long courseId,
            @RequestBody CreateWeekRequestDto request,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("주차 생성 요청: courseId={}, weekNumber={}, professorId={}",
                courseId, request.getWeekNumber(), professorId);

        WeekDto response = courseWeekContentService.createWeek(courseId, request, professorId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /**
     * 주차 수정
     */
    @PutMapping("/{weekId}")
    public ResponseEntity<ApiResponse<WeekDto>> updateWeek(
            @PathVariable Long courseId,
            @PathVariable Long weekId,
            @RequestBody UpdateWeekRequestDto request,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("주차 수정 요청: courseId={}, weekId={}, professorId={}", courseId, weekId, professorId);

        WeekDto response = courseWeekContentService.updateWeek(courseId, weekId, request, professorId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 주차 삭제
     */
    @DeleteMapping("/{weekId}")
    public ResponseEntity<ApiResponse<Void>> deleteWeek(
            @PathVariable Long courseId,
            @PathVariable Long weekId,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("주차 삭제 요청: courseId={}, weekId={}, professorId={}", courseId, weekId, professorId);

        courseWeekContentService.deleteWeek(courseId, weekId, professorId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 콘텐츠 등록
     */
    @PostMapping("/{weekId}/contents")
    public ResponseEntity<ApiResponse<WeekContentDto>> createContent(
            @PathVariable Long courseId,
            @PathVariable Long weekId,
            @RequestBody CreateWeekContentRequestDto request,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("콘텐츠 등록 요청: courseId={}, weekId={}, contentType={}, professorId={}",
                courseId, weekId, request.getContentType(), professorId);

        WeekContentDto response = courseWeekContentService.createContent(courseId, weekId, request, professorId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /**
     * 콘텐츠 수정
     */
    @PutMapping("/{weekId}/contents/{contentId}")
    public ResponseEntity<ApiResponse<WeekContentDto>> updateContent(
            @PathVariable Long courseId,
            @PathVariable Long weekId,
            @PathVariable Long contentId,
            @RequestBody UpdateWeekContentRequestDto request,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("콘텐츠 수정 요청: courseId={}, weekId={}, contentId={}, professorId={}",
                courseId, weekId, contentId, professorId);

        WeekContentDto response = courseWeekContentService.updateContent(
                courseId, weekId, contentId, request, professorId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 콘텐츠 삭제
     */
    @DeleteMapping("/{weekId}/contents/{contentId}")
    public ResponseEntity<ApiResponse<Void>> deleteContent(
            @PathVariable Long courseId,
            @PathVariable Long weekId,
            @PathVariable Long contentId,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("콘텐츠 삭제 요청: courseId={}, weekId={}, contentId={}, professorId={}",
                courseId, weekId, contentId, professorId);

        courseWeekContentService.deleteContent(courseId, weekId, contentId, professorId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 강의 주차 목록 조회 (교수/수강중 학생)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<WeekListResponseDto>>> getWeeks(
            @PathVariable Long courseId,
            @AuthenticationPrincipal Long requesterId) {
        if (requesterId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("강의 주차 목록 조회: courseId={}, requesterId={}", courseId, requesterId);

        List<WeekListResponseDto> response = courseWeekContentService.getWeeks(courseId, requesterId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 주차별 콘텐츠 목록 조회
     */
    @GetMapping("/{weekId}/contents")
    public ResponseEntity<ApiResponse<WeekContentsResponseDto>> getWeekContents(
            @PathVariable Long courseId,
            @PathVariable Long weekId,
            @AuthenticationPrincipal Long requesterId) {
        if (requesterId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("주차별 콘텐츠 목록 조회: courseId={}, weekId={}, requesterId={}",
                courseId, weekId, requesterId);

        WeekContentsResponseDto response = courseWeekContentService.getWeekContents(
                courseId, weekId, requesterId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

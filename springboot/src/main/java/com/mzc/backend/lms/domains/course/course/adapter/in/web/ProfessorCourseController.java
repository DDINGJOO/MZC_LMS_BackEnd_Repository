package com.mzc.backend.lms.domains.course.course.adapter.in.web;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.course.course.application.port.in.ProfessorCourseUseCase;
import com.mzc.backend.lms.domains.course.course.adapter.in.web.dto.*;
import com.mzc.backend.lms.domains.user.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 교수 강의 관리 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/professor/courses")
@RequiredArgsConstructor
public class ProfessorCourseController {

    private final ProfessorCourseUseCase professorCourseUseCase;

    /**
     * 강의 개설
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CreateCourseResponseDto>> createCourse(
            @RequestBody CreateCourseRequestDto request,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("강의 개설 요청: professorId={}", professorId);

        CreateCourseResponseDto response = professorCourseUseCase.createCourse(request, professorId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /**
     * 강의 수정
     */
    @PutMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CreateCourseResponseDto>> updateCourse(
            @PathVariable Long courseId,
            @RequestBody UpdateCourseRequestDto request,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("강의 수정 요청: courseId={}, professorId={}", courseId, professorId);

        CreateCourseResponseDto response = professorCourseUseCase.updateCourse(courseId, request, professorId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 강의 취소
     */
    @DeleteMapping("/{courseId}")
    public ResponseEntity<ApiResponse<Void>> cancelCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("강의 취소 요청: courseId={}, professorId={}", courseId, professorId);

        professorCourseUseCase.cancelCourse(courseId, professorId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 내가 개설한 강의 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<MyCoursesResponseDto>> getMyCourses(
            @RequestParam(required = false) Long academicTermId,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("내 강의 목록 조회: professorId={}, academicTermId={}", professorId, academicTermId);

        MyCoursesResponseDto response = professorCourseUseCase.getMyCourses(professorId, academicTermId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 교수 강의 상세 조회
     */
    @GetMapping("/{courseId}")
    public ResponseEntity<ApiResponse<ProfessorCourseDetailDto>> getCourseDetail(
            @PathVariable Long courseId,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("교수 강의 상세 조회: courseId={}, professorId={}", courseId, professorId);

        ProfessorCourseDetailDto response = professorCourseUseCase.getCourseDetail(courseId, professorId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

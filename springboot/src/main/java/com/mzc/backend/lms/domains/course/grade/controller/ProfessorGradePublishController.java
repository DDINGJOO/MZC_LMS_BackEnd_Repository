package com.mzc.backend.lms.domains.course.grade.controller;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.course.grade.service.GradePublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 교수용 성적 산출/확정/공개 수동 실행 API
 *
 * 정책:
 * - 관리자 없이, PROFESSOR 권한이면 "아무 교수나" 실행 가능
 * - 단, 성적 공개 기간(GRADE_PUBLISH) 중에만 실행 가능
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/professor/grades")
@RequiredArgsConstructor
public class ProfessorGradePublishController {

    private final GradePublishService gradePublishService;

    /**
     * (배치) 성적 공개 기간(GRADE_PUBLISH) 중인 학기의 강의들을 대상으로 공개 로직을 즉시 실행
     * - 교수 버튼(수동)용
     */
    @PostMapping("/publish-ended-terms")
    public ResponseEntity<ApiResponse<Void>> publishEndedTerms(Authentication authentication) {
        // 권한은 SecurityConfig에서 /api/v1/professor/** 로 제한됨
        gradePublishService.publishEndedTerms(LocalDateTime.now());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 특정 학기의 성적 공개(수동)
     */
    @PostMapping("/publish/terms/{academicTermId}")
    public ResponseEntity<ApiResponse<Void>> publishTerm(
            @PathVariable Long academicTermId,
            Authentication authentication
    ) {
        gradePublishService.publishTermIfAllowed(academicTermId, LocalDateTime.now());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

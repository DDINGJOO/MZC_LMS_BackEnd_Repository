package com.mzc.backend.lms.domains.assessment.controller;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.assessment.dto.request.AttemptSubmitRequestDto;
import com.mzc.backend.lms.domains.assessment.dto.response.*;
import com.mzc.backend.lms.domains.assessment.enums.AssessmentType;
import com.mzc.backend.lms.domains.assessment.service.AssessmentService;
import com.mzc.backend.lms.domains.user.auth.exception.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 학생용 시험/퀴즈 조회/응시 API
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AssessmentStudentController {

    private final AssessmentService assessmentService;

    /**
     * 시험/퀴즈 목록 조회 (학생)
     * - 명세서 호환: GET /api/v1/exams?courseId=...&examType=QUIZ
     * - 학생에게는 시작시간 전 항목 숨김
     */
    @GetMapping("/api/v1/exams")
    public ResponseEntity<ApiResponse<List<AssessmentListItemResponseDto>>> list(
            @RequestParam Long courseId,
            @RequestParam AssessmentType examType,
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }
        List<AssessmentListItemResponseDto> data = assessmentService.listForStudent(courseId, examType, studentId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    /**
     * 시험/퀴즈 상세 조회 (학생)
     * - 학생에게는 정답 마스킹된 questionData 제공
     */
    @GetMapping("/api/v1/exams/{examId}")
    public ResponseEntity<ApiResponse<AssessmentDetailResponseDto>> detail(
            @PathVariable Long examId,
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }
        AssessmentDetailResponseDto data = assessmentService.getDetailForStudent(examId, studentId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    /**
     * 응시 시작 (학생)
     * - 예: POST /api/v1/exams/{examId}/start
     * - QuizAttempt = exam_results 생성/조회
     */
    @PostMapping("/api/v1/exams/{examId}/start")
    public ResponseEntity<ApiResponse<AttemptStartResponseDto>> start(
            @PathVariable Long examId,
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }
        AttemptStartResponseDto data = assessmentService.startAttempt(examId, studentId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    /**
     * 최종 제출 (학생)
     * - late면 0점 처리
     */
    @PostMapping("/api/v1/exams/results/{attemptId}/submit")
    public ResponseEntity<ApiResponse<AttemptSubmitResponseDto>> submit(
            @PathVariable Long attemptId,
            @Valid @RequestBody AttemptSubmitRequestDto request,
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }
        AttemptSubmitResponseDto data = assessmentService.submitAttempt(attemptId, request, studentId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}

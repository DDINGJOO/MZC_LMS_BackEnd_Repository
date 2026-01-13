package com.mzc.backend.lms.domains.assessment.adapter.in.web;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.request.AssessmentCreateRequestDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.request.AssessmentUpdateRequestDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.request.AttemptGradeRequestDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.response.AssessmentDetailResponseDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.response.AssessmentListItemResponseDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.response.AttemptGradeResponseDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.response.ProfessorAttemptDetailResponseDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.response.ProfessorAttemptListItemResponseDto;
import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.enums.AssessmentType;
import com.mzc.backend.lms.domains.assessment.application.service.AssessmentService;
import com.mzc.backend.lms.domains.board.enums.BoardType;
import com.mzc.backend.lms.domains.user.exception.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 교수용 시험/퀴즈 관리 API
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AssessmentProfessorController {

    private final AssessmentService assessmentService;

    /**
     * 시험/퀴즈 목록 조회 (교수)
     * - 예: GET /api/v1/professor/exams?courseId=101&examType=QUIZ
     * - 교수는 시작 전 포함 전체 조회 가능(미리보기)
     */
    @GetMapping("/api/v1/professor/exams")
    public ResponseEntity<ApiResponse<List<AssessmentListItemResponseDto>>> list(
            @RequestParam Long courseId,
            @RequestParam AssessmentType examType,
            @AuthenticationPrincipal Long professorId
    ) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }
        List<AssessmentListItemResponseDto> data = assessmentService.listForProfessor(courseId, examType, professorId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    /**
     * 시험/퀴즈 상세 조회 (교수)
     * - 예: GET /api/v1/professor/exams/{examId}
     * - 교수는 questionData(정답 포함) 원본 조회 가능
     */
    @GetMapping("/api/v1/professor/exams/{examId}")
    public ResponseEntity<ApiResponse<AssessmentDetailResponseDto>> detail(
            @PathVariable Long examId,
            @AuthenticationPrincipal Long professorId
    ) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }
        AssessmentDetailResponseDto data = assessmentService.getDetailForProfessor(examId, professorId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    /**
     * 응시자/응시 결과 목록 조회 (교수)
     * - 예: GET /api/v1/professor/exams/{examId}/attempts?status=ALL
     * - status(optional): ALL | SUBMITTED | IN_PROGRESS (기본값 ALL)
     */
    @GetMapping("/api/v1/professor/exams/{examId}/attempts")
    public ResponseEntity<ApiResponse<List<ProfessorAttemptListItemResponseDto>>> listAttempts(
            @PathVariable Long examId,
            @RequestParam(required = false, defaultValue = "ALL") String status,
            @AuthenticationPrincipal Long professorId
    ) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }
        List<ProfessorAttemptListItemResponseDto> data =
                assessmentService.listAttemptsForProfessor(examId, status, professorId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    /**
     * 응시 결과 상세 조회(답안 포함) (교수)
     * - 예: GET /api/v1/professor/exams/results/{attemptId}
     */
    @GetMapping("/api/v1/professor/exams/results/{attemptId}")
    public ResponseEntity<ApiResponse<ProfessorAttemptDetailResponseDto>> attemptDetail(
            @PathVariable Long attemptId,
            @AuthenticationPrincipal Long professorId
    ) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }
        ProfessorAttemptDetailResponseDto data =
                assessmentService.getAttemptDetailForProfessor(attemptId, professorId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    /**
     * 시험/퀴즈 등록 (교수)
     * - 예: POST /api/v1/boards/QUIZ/exams
     */
    @PostMapping("/api/v1/boards/{boardType}/exams")
    public ResponseEntity<ApiResponse<AssessmentDetailResponseDto>> create(
            @PathVariable String boardType,
            @Valid @RequestBody AssessmentCreateRequestDto request,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }
        BoardType bt = BoardType.valueOf(boardType);
        AssessmentDetailResponseDto data = assessmentService.create(bt, request, professorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(data));
    }

    /**
     * 시험/퀴즈 수정 (교수)
     * - 예: PUT /api/v1/exams/{examId}/edit
     */
    @PutMapping("/api/v1/exams/{examId}/edit")
    public ResponseEntity<ApiResponse<AssessmentDetailResponseDto>> update(
            @PathVariable Long examId,
            @Valid @RequestBody AssessmentUpdateRequestDto request,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }
        AssessmentDetailResponseDto data = assessmentService.update(examId, request, professorId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    /**
     * 시험/퀴즈 삭제 (교수)
     * - 예: DELETE /api/v1/exams/{examId}/delete
     */
    @DeleteMapping("/api/v1/exams/{examId}/delete")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long examId,
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }
        assessmentService.delete(examId, professorId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 시험 채점 (교수)
     * - 퀴즈는 자동채점이므로 본 API 대상이 아님
     */
    @PutMapping("/api/v1/exams/results/{attemptId}/grade")
    public ResponseEntity<ApiResponse<AttemptGradeResponseDto>> gradeAttempt(
            @PathVariable Long attemptId,
            @Valid @RequestBody AttemptGradeRequestDto request,
            @AuthenticationPrincipal Long professorId
    ) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }
        AttemptGradeResponseDto data = assessmentService.gradeAttempt(attemptId, request, professorId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}

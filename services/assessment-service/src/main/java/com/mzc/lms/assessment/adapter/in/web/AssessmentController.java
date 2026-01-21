package com.mzc.lms.assessment.adapter.in.web;

import com.mzc.lms.assessment.adapter.in.web.dto.*;
import com.mzc.lms.assessment.application.port.in.*;
import com.mzc.lms.assessment.domain.model.Assessment;
import com.mzc.lms.assessment.domain.model.AssessmentType;
import com.mzc.lms.assessment.domain.model.Question;
import com.mzc.lms.assessment.domain.model.Submission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/assessments")
@RequiredArgsConstructor
@Tag(name = "Assessment", description = "평가 관리 API")
public class AssessmentController {

    private final AssessmentUseCase assessmentUseCase;
    private final QuestionUseCase questionUseCase;
    private final SubmissionUseCase submissionUseCase;

    // ========== Assessment APIs ==========

    @PostMapping
    @Operation(summary = "평가 생성", description = "새로운 평가를 생성합니다")
    public ResponseEntity<AssessmentResponse> createAssessment(@Valid @RequestBody CreateAssessmentRequest request) {
        Assessment assessment = assessmentUseCase.createAssessment(
                request.getCourseId(),
                request.getTitle(),
                request.getDescription(),
                request.getType(),
                request.getTotalPoints(),
                request.getPassingPoints()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(AssessmentResponse.from(assessment));
    }

    @GetMapping("/{id}")
    @Operation(summary = "평가 조회", description = "평가 ID로 평가를 조회합니다")
    public ResponseEntity<AssessmentResponse> getAssessment(@PathVariable Long id) {
        return assessmentUseCase.findById(id)
                .map(assessment -> ResponseEntity.ok(AssessmentResponse.from(assessment)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "코스별 평가 목록 조회", description = "코스 ID로 평가 목록을 조회합니다")
    public ResponseEntity<List<AssessmentResponse>> getAssessmentsByCourse(
            @PathVariable Long courseId,
            @RequestParam(required = false) AssessmentType type) {
        List<Assessment> assessments;
        if (type != null) {
            assessments = assessmentUseCase.findByCourseIdAndType(courseId, type);
        } else {
            assessments = assessmentUseCase.findByCourseId(courseId);
        }
        List<AssessmentResponse> responses = assessments.stream()
                .map(AssessmentResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/course/{courseId}/available")
    @Operation(summary = "코스별 응시 가능 평가 목록 조회", description = "코스 ID로 응시 가능한 평가 목록을 조회합니다")
    public ResponseEntity<List<AssessmentResponse>> getAvailableAssessmentsByCourse(@PathVariable Long courseId) {
        List<AssessmentResponse> responses = assessmentUseCase.findAvailableAssessments(courseId).stream()
                .map(AssessmentResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @Operation(summary = "평가 수정", description = "평가 정보를 수정합니다")
    public ResponseEntity<AssessmentResponse> updateAssessment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAssessmentRequest request) {
        Assessment assessment = assessmentUseCase.updateAssessment(
                id,
                request.getTitle(),
                request.getDescription(),
                request.getTotalPoints(),
                request.getPassingPoints(),
                request.getTimeLimitMinutes()
        );
        return ResponseEntity.ok(AssessmentResponse.from(assessment));
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "평가 발행", description = "평가를 발행합니다")
    public ResponseEntity<AssessmentResponse> publishAssessment(@PathVariable Long id) {
        Assessment assessment = assessmentUseCase.publishAssessment(id);
        return ResponseEntity.ok(AssessmentResponse.from(assessment));
    }

    @PostMapping("/{id}/schedule")
    @Operation(summary = "평가 일정 설정", description = "평가 시작/종료 일정을 설정합니다")
    public ResponseEntity<AssessmentResponse> setAssessmentSchedule(
            @PathVariable Long id,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        Assessment assessment = assessmentUseCase.setSchedule(
                id,
                java.time.LocalDateTime.parse(startDate),
                java.time.LocalDateTime.parse(endDate)
        );
        return ResponseEntity.ok(AssessmentResponse.from(assessment));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "평가 삭제", description = "평가를 삭제합니다")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        assessmentUseCase.deleteAssessment(id);
        return ResponseEntity.noContent().build();
    }

    // ========== Question APIs ==========

    @PostMapping("/{assessmentId}/questions")
    @Operation(summary = "문제 생성", description = "평가에 문제를 추가합니다")
    public ResponseEntity<QuestionResponse> createQuestion(
            @PathVariable Long assessmentId,
            @Valid @RequestBody CreateQuestionRequest request) {
        Question question = questionUseCase.createQuestion(
                assessmentId,
                request.getType(),
                request.getQuestionText(),
                request.getOptions(),
                request.getCorrectAnswer(),
                request.getPoints(),
                request.getOrderIndex()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(QuestionResponse.from(question));
    }

    @GetMapping("/{assessmentId}/questions")
    @Operation(summary = "평가별 문제 목록 조회", description = "평가 ID로 문제 목록을 조회합니다")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByAssessment(
            @PathVariable Long assessmentId,
            @RequestParam(defaultValue = "false") boolean includeAnswers) {
        List<Question> questions = questionUseCase.findByAssessmentId(assessmentId);
        List<QuestionResponse> responses = questions.stream()
                .map(q -> includeAnswers ? QuestionResponse.from(q) : QuestionResponse.fromWithoutAnswer(q))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/questions/{id}")
    @Operation(summary = "문제 조회", description = "문제 ID로 문제를 조회합니다")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable Long id) {
        return questionUseCase.findById(id)
                .map(question -> ResponseEntity.ok(QuestionResponse.from(question)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/questions/{id}")
    @Operation(summary = "문제 수정", description = "문제 정보를 수정합니다")
    public ResponseEntity<QuestionResponse> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody UpdateQuestionRequest request) {
        Question question = questionUseCase.updateQuestion(
                id,
                request.getQuestionText(),
                request.getOptions(),
                request.getCorrectAnswer(),
                request.getPoints(),
                request.getExplanation()
        );
        return ResponseEntity.ok(QuestionResponse.from(question));
    }

    @DeleteMapping("/questions/{id}")
    @Operation(summary = "문제 삭제", description = "문제를 삭제합니다")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionUseCase.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{assessmentId}/questions/reorder")
    @Operation(summary = "문제 순서 변경", description = "문제의 순서를 변경합니다")
    public ResponseEntity<Void> reorderQuestions(
            @PathVariable Long assessmentId,
            @RequestBody List<Long> questionIds) {
        questionUseCase.reorderQuestions(assessmentId, questionIds);
        return ResponseEntity.ok().build();
    }

    // ========== Submission APIs ==========

    @PostMapping("/{assessmentId}/submissions")
    @Operation(summary = "제출 생성", description = "학생의 평가 제출을 생성합니다")
    public ResponseEntity<SubmissionResponse> createSubmission(
            @PathVariable Long assessmentId,
            @RequestParam Long studentId) {
        Submission submission = submissionUseCase.createSubmission(assessmentId, studentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(SubmissionResponse.from(submission));
    }

    @PostMapping("/submissions/{submissionId}/start")
    @Operation(summary = "제출 시작", description = "학생의 평가 응시를 시작합니다")
    public ResponseEntity<SubmissionResponse> startSubmission(@PathVariable Long submissionId) {
        Submission submission = submissionUseCase.startSubmission(submissionId);
        return ResponseEntity.ok(SubmissionResponse.from(submission));
    }

    @PostMapping("/submissions/{submissionId}/save")
    @Operation(summary = "답안 저장", description = "답안을 임시 저장합니다")
    public ResponseEntity<SubmissionResponse> saveAnswers(
            @PathVariable Long submissionId,
            @Valid @RequestBody SubmitAnswersRequest request) {
        Submission submission = submissionUseCase.saveAnswers(submissionId, request.getAnswers());
        return ResponseEntity.ok(SubmissionResponse.from(submission));
    }

    @PostMapping("/submissions/{submissionId}/submit")
    @Operation(summary = "제출 완료", description = "평가를 최종 제출합니다")
    public ResponseEntity<SubmissionResponse> submitSubmission(@PathVariable Long submissionId) {
        Submission submission = submissionUseCase.submitSubmission(submissionId);
        return ResponseEntity.ok(SubmissionResponse.from(submission));
    }

    @PostMapping("/submissions/{submissionId}/grade/auto")
    @Operation(summary = "자동 채점", description = "객관식/참거짓 문제를 자동 채점합니다")
    public ResponseEntity<SubmissionResponse> autoGradeSubmission(@PathVariable Long submissionId) {
        Submission submission = submissionUseCase.autoGradeSubmission(submissionId);
        return ResponseEntity.ok(SubmissionResponse.from(submission));
    }

    @PostMapping("/submissions/{submissionId}/grade/manual")
    @Operation(summary = "수동 채점", description = "평가를 수동으로 채점합니다")
    public ResponseEntity<SubmissionResponse> manualGradeSubmission(
            @PathVariable Long submissionId,
            @Valid @RequestBody GradeSubmissionRequest request) {
        Submission submission = submissionUseCase.gradeSubmission(
                submissionId,
                request.getEarnedPoints(),
                request.getFeedback(),
                request.getGradedBy()
        );
        return ResponseEntity.ok(SubmissionResponse.from(submission));
    }

    @GetMapping("/submissions/{submissionId}")
    @Operation(summary = "제출 조회", description = "제출 ID로 제출을 조회합니다")
    public ResponseEntity<SubmissionResponse> getSubmission(@PathVariable Long submissionId) {
        return submissionUseCase.findById(submissionId)
                .map(submission -> ResponseEntity.ok(SubmissionResponse.from(submission)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{assessmentId}/submissions")
    @Operation(summary = "평가별 제출 목록 조회", description = "평가 ID로 제출 목록을 조회합니다")
    public ResponseEntity<List<SubmissionResponse>> getSubmissionsByAssessment(@PathVariable Long assessmentId) {
        List<SubmissionResponse> responses = submissionUseCase.findByAssessmentId(assessmentId).stream()
                .map(SubmissionResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{assessmentId}/submissions/student/{studentId}")
    @Operation(summary = "학생별 제출 조회", description = "평가 ID와 학생 ID로 제출을 조회합니다")
    public ResponseEntity<SubmissionResponse> getSubmissionByStudent(
            @PathVariable Long assessmentId,
            @PathVariable Long studentId) {
        return submissionUseCase.findByAssessmentIdAndStudentId(assessmentId, studentId)
                .map(submission -> ResponseEntity.ok(SubmissionResponse.from(submission)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/submissions/student/{studentId}")
    @Operation(summary = "학생의 전체 제출 목록 조회", description = "학생 ID로 모든 제출 목록을 조회합니다")
    public ResponseEntity<List<SubmissionResponse>> getAllSubmissionsByStudent(@PathVariable Long studentId) {
        List<SubmissionResponse> responses = submissionUseCase.findByStudentId(studentId).stream()
                .map(SubmissionResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{assessmentId}/submissions/student/{studentId}/count")
    @Operation(summary = "학생 응시 횟수 조회", description = "학생의 특정 평가 응시 횟수를 조회합니다")
    public ResponseEntity<Integer> getAttemptCount(
            @PathVariable Long assessmentId,
            @PathVariable Long studentId) {
        Integer count = submissionUseCase.getAttemptCount(assessmentId, studentId);
        return ResponseEntity.ok(count);
    }
}

package com.mzc.lms.enrollment.adapter.in.web;

import com.mzc.lms.enrollment.adapter.in.web.dto.*;
import com.mzc.lms.enrollment.application.port.in.EnrollmentHistoryUseCase;
import com.mzc.lms.enrollment.application.port.in.EnrollmentUseCase;
import com.mzc.lms.enrollment.domain.model.Enrollment;
import com.mzc.lms.enrollment.domain.model.EnrollmentStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollment", description = "수강 신청 관리 API")
public class EnrollmentController {

    private final EnrollmentUseCase enrollmentUseCase;
    private final EnrollmentHistoryUseCase historyUseCase;

    @PostMapping
    @Operation(summary = "수강 신청", description = "학생의 강좌 수강 신청을 생성합니다")
    public ResponseEntity<EnrollmentResponse> createEnrollment(
            @Valid @RequestBody CreateEnrollmentRequest request) {
        log.info("Creating enrollment for student: {} and course: {}",
                request.getStudentId(), request.getCourseId());

        Enrollment enrollment = enrollmentUseCase.createEnrollment(
                request.getStudentId(), request.getCourseId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EnrollmentResponse.from(enrollment));
    }

    @GetMapping("/{id}")
    @Operation(summary = "수강 신청 조회", description = "수강 신청 ID로 조회합니다")
    public ResponseEntity<EnrollmentResponse> getEnrollment(
            @Parameter(description = "수강 신청 ID") @PathVariable Long id) {
        return enrollmentUseCase.findById(id)
                .map(enrollment -> ResponseEntity.ok(EnrollmentResponse.from(enrollment)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "수강 확정", description = "대기 중인 수강 신청을 확정합니다")
    public ResponseEntity<EnrollmentResponse> confirmEnrollment(
            @Parameter(description = "수강 신청 ID") @PathVariable Long id) {
        log.info("Confirming enrollment: {}", id);

        Enrollment enrollment = enrollmentUseCase.confirmEnrollment(id);
        return ResponseEntity.ok(EnrollmentResponse.from(enrollment));
    }

    @PostMapping("/{id}/waitlist")
    @Operation(summary = "대기 목록 등록", description = "수강 신청을 대기 목록에 등록합니다")
    public ResponseEntity<EnrollmentResponse> waitlistEnrollment(
            @Parameter(description = "수강 신청 ID") @PathVariable Long id) {
        log.info("Waitlisting enrollment: {}", id);

        Enrollment enrollment = enrollmentUseCase.waitlistEnrollment(id);
        return ResponseEntity.ok(EnrollmentResponse.from(enrollment));
    }

    @PostMapping("/{id}/withdraw")
    @Operation(summary = "수강 철회", description = "수강 신청을 철회합니다")
    public ResponseEntity<EnrollmentResponse> withdrawEnrollment(
            @Parameter(description = "수강 신청 ID") @PathVariable Long id,
            @Valid @RequestBody WithdrawEnrollmentRequest request) {
        log.info("Withdrawing enrollment: {} with reason: {}", id, request.getReason());

        Enrollment enrollment = enrollmentUseCase.withdrawEnrollment(id, request.getReason());
        return ResponseEntity.ok(EnrollmentResponse.from(enrollment));
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "수강 완료", description = "수강을 완료 처리하고 성적을 부여합니다")
    public ResponseEntity<EnrollmentResponse> completeEnrollment(
            @Parameter(description = "수강 신청 ID") @PathVariable Long id,
            @Valid @RequestBody CompleteEnrollmentRequest request) {
        log.info("Completing enrollment: {} with grade: {}", id, request.getGrade());

        Enrollment enrollment = enrollmentUseCase.completeEnrollment(
                id, request.getGrade(), request.getGradePoint());
        return ResponseEntity.ok(EnrollmentResponse.from(enrollment));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "수강 취소", description = "대기 중인 수강 신청을 취소합니다")
    public ResponseEntity<EnrollmentResponse> cancelEnrollment(
            @Parameter(description = "수강 신청 ID") @PathVariable Long id) {
        log.info("Cancelling enrollment: {}", id);

        Enrollment enrollment = enrollmentUseCase.cancelEnrollment(id);
        return ResponseEntity.ok(EnrollmentResponse.from(enrollment));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "학생별 수강 목록", description = "특정 학생의 모든 수강 신청을 조회합니다")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByStudent(
            @Parameter(description = "학생 ID") @PathVariable Long studentId) {
        List<EnrollmentResponse> enrollments = enrollmentUseCase.findByStudentId(studentId)
                .stream()
                .map(EnrollmentResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/student/{studentId}/status/{status}")
    @Operation(summary = "학생별 상태별 수강 목록", description = "특정 학생의 특정 상태 수강 신청을 조회합니다")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByStudentAndStatus(
            @Parameter(description = "학생 ID") @PathVariable Long studentId,
            @Parameter(description = "수강 상태") @PathVariable EnrollmentStatus status) {
        List<EnrollmentResponse> enrollments = enrollmentUseCase.findByStudentIdAndStatus(studentId, status)
                .stream()
                .map(EnrollmentResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "강좌별 수강 목록", description = "특정 강좌의 모든 수강 신청을 조회합니다")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByCourse(
            @Parameter(description = "강좌 ID") @PathVariable Long courseId) {
        List<EnrollmentResponse> enrollments = enrollmentUseCase.findByCourseId(courseId)
                .stream()
                .map(EnrollmentResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/course/{courseId}/status/{status}")
    @Operation(summary = "강좌별 상태별 수강 목록", description = "특정 강좌의 특정 상태 수강 신청을 조회합니다")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByCourseAndStatus(
            @Parameter(description = "강좌 ID") @PathVariable Long courseId,
            @Parameter(description = "수강 상태") @PathVariable EnrollmentStatus status) {
        List<EnrollmentResponse> enrollments = enrollmentUseCase.findByCourseIdAndStatus(courseId, status)
                .stream()
                .map(EnrollmentResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/course/{courseId}/waitlist")
    @Operation(summary = "강좌 대기자 목록", description = "특정 강좌의 대기자 목록을 조회합니다 (신청 순)")
    public ResponseEntity<List<EnrollmentResponse>> getWaitlistedEnrollments(
            @Parameter(description = "강좌 ID") @PathVariable Long courseId) {
        List<EnrollmentResponse> enrollments = enrollmentUseCase.findWaitlistedByCourseId(courseId)
                .stream()
                .map(EnrollmentResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    @Operation(summary = "학생-강좌 수강 조회", description = "특정 학생의 특정 강좌 수강 신청을 조회합니다")
    public ResponseEntity<EnrollmentResponse> getEnrollmentByStudentAndCourse(
            @Parameter(description = "학생 ID") @PathVariable Long studentId,
            @Parameter(description = "강좌 ID") @PathVariable Long courseId) {
        return enrollmentUseCase.findByStudentIdAndCourseId(studentId, courseId)
                .map(enrollment -> ResponseEntity.ok(EnrollmentResponse.from(enrollment)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}/course/{courseId}/enrolled")
    @Operation(summary = "수강 여부 확인", description = "학생이 해당 강좌에 수강 중인지 확인합니다")
    public ResponseEntity<Boolean> isStudentEnrolled(
            @Parameter(description = "학생 ID") @PathVariable Long studentId,
            @Parameter(description = "강좌 ID") @PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentUseCase.isStudentEnrolled(studentId, courseId));
    }

    @GetMapping("/course/{courseId}/count")
    @Operation(summary = "강좌 수강 인원", description = "특정 강좌의 특정 상태 수강 인원을 조회합니다")
    public ResponseEntity<Long> countEnrollments(
            @Parameter(description = "강좌 ID") @PathVariable Long courseId,
            @Parameter(description = "수강 상태") @RequestParam EnrollmentStatus status) {
        return ResponseEntity.ok(enrollmentUseCase.countByCourseIdAndStatus(courseId, status));
    }

    @GetMapping("/{id}/history")
    @Operation(summary = "수강 이력 조회", description = "수강 신청의 상태 변경 이력을 조회합니다")
    public ResponseEntity<List<EnrollmentHistoryResponse>> getEnrollmentHistory(
            @Parameter(description = "수강 신청 ID") @PathVariable Long id) {
        List<EnrollmentHistoryResponse> histories = historyUseCase.findByEnrollmentId(id)
                .stream()
                .map(EnrollmentHistoryResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(histories);
    }
}

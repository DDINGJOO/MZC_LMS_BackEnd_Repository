package com.mzc.lms.progress.adapter.in.web;

import com.mzc.lms.progress.adapter.in.web.dto.*;
import com.mzc.lms.progress.application.port.in.ContentProgressUseCase;
import com.mzc.lms.progress.application.port.in.LearningProgressUseCase;
import com.mzc.lms.progress.domain.model.LearningProgress;
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
@RequestMapping("/progress")
@RequiredArgsConstructor
@Tag(name = "Learning Progress", description = "학습 진도 관리 API")
public class LearningProgressController {

    private final LearningProgressUseCase progressUseCase;
    private final ContentProgressUseCase contentProgressUseCase;

    @PostMapping
    @Operation(summary = "학습 진도 생성", description = "새로운 학습 진도를 생성합니다")
    public ResponseEntity<LearningProgressResponse> createProgress(
            @Valid @RequestBody CreateProgressRequest request) {
        log.info("Creating progress for student: {}, course: {}",
                request.getStudentId(), request.getCourseId());

        LearningProgress progress = progressUseCase.createProgress(
                request.getStudentId(),
                request.getCourseId(),
                request.getEnrollmentId(),
                request.getTotalContents());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LearningProgressResponse.from(progress));
    }

    @GetMapping("/{id}")
    @Operation(summary = "학습 진도 조회", description = "학습 진도 ID로 조회합니다")
    public ResponseEntity<LearningProgressResponse> getProgress(
            @Parameter(description = "학습 진도 ID") @PathVariable Long id) {
        return progressUseCase.findById(id)
                .map(progress -> ResponseEntity.ok(LearningProgressResponse.from(progress)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/start")
    @Operation(summary = "학습 시작", description = "학습을 시작합니다")
    public ResponseEntity<LearningProgressResponse> startLearning(
            @Parameter(description = "학습 진도 ID") @PathVariable Long id) {
        log.info("Starting learning for progress: {}", id);

        LearningProgress progress = progressUseCase.startLearning(id);
        return ResponseEntity.ok(LearningProgressResponse.from(progress));
    }

    @PutMapping("/{id}")
    @Operation(summary = "학습 진도 업데이트", description = "학습 진도를 업데이트합니다")
    public ResponseEntity<LearningProgressResponse> updateProgress(
            @Parameter(description = "학습 진도 ID") @PathVariable Long id,
            @Valid @RequestBody UpdateProgressRequest request) {
        log.info("Updating progress: {} with completed: {}", id, request.getCompletedContents());

        Long additionalTime = request.getAdditionalTimeSeconds() != null ? request.getAdditionalTimeSeconds() : 0L;
        LearningProgress progress = progressUseCase.updateProgress(id, request.getCompletedContents(), additionalTime);
        return ResponseEntity.ok(LearningProgressResponse.from(progress));
    }

    @PostMapping("/{id}/time")
    @Operation(summary = "학습 시간 추가", description = "학습 시간을 추가합니다")
    public ResponseEntity<LearningProgressResponse> addLearningTime(
            @Parameter(description = "학습 진도 ID") @PathVariable Long id,
            @Parameter(description = "추가 시간(초)") @RequestParam Long seconds) {
        log.info("Adding {} seconds to progress: {}", seconds, id);

        LearningProgress progress = progressUseCase.addLearningTime(id, seconds);
        return ResponseEntity.ok(LearningProgressResponse.from(progress));
    }

    @PostMapping("/{id}/pause")
    @Operation(summary = "학습 일시 중지", description = "학습을 일시 중지합니다")
    public ResponseEntity<LearningProgressResponse> pauseLearning(
            @Parameter(description = "학습 진도 ID") @PathVariable Long id) {
        log.info("Pausing learning for progress: {}", id);

        LearningProgress progress = progressUseCase.pauseLearning(id);
        return ResponseEntity.ok(LearningProgressResponse.from(progress));
    }

    @PostMapping("/{id}/resume")
    @Operation(summary = "학습 재개", description = "일시 중지된 학습을 재개합니다")
    public ResponseEntity<LearningProgressResponse> resumeLearning(
            @Parameter(description = "학습 진도 ID") @PathVariable Long id) {
        log.info("Resuming learning for progress: {}", id);

        LearningProgress progress = progressUseCase.resumeLearning(id);
        return ResponseEntity.ok(LearningProgressResponse.from(progress));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "학생별 학습 진도", description = "특정 학생의 모든 학습 진도를 조회합니다")
    public ResponseEntity<List<LearningProgressResponse>> getProgressByStudent(
            @Parameter(description = "학생 ID") @PathVariable Long studentId) {
        List<LearningProgressResponse> progressList = progressUseCase.findByStudentId(studentId)
                .stream()
                .map(LearningProgressResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(progressList);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "강좌별 학습 진도", description = "특정 강좌의 모든 학습 진도를 조회합니다")
    public ResponseEntity<List<LearningProgressResponse>> getProgressByCourse(
            @Parameter(description = "강좌 ID") @PathVariable Long courseId) {
        List<LearningProgressResponse> progressList = progressUseCase.findByCourseId(courseId)
                .stream()
                .map(LearningProgressResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(progressList);
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    @Operation(summary = "학생-강좌 학습 진도", description = "특정 학생의 특정 강좌 학습 진도를 조회합니다")
    public ResponseEntity<LearningProgressResponse> getProgressByStudentAndCourse(
            @Parameter(description = "학생 ID") @PathVariable Long studentId,
            @Parameter(description = "강좌 ID") @PathVariable Long courseId) {
        return progressUseCase.findByStudentIdAndCourseId(studentId, courseId)
                .map(progress -> ResponseEntity.ok(LearningProgressResponse.from(progress)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/course/{courseId}/average")
    @Operation(summary = "강좌 평균 진도", description = "특정 강좌의 평균 진도율을 조회합니다")
    public ResponseEntity<Double> getAverageProgress(
            @Parameter(description = "강좌 ID") @PathVariable Long courseId) {
        Double average = progressUseCase.getAverageProgressByCourseId(courseId);
        return ResponseEntity.ok(average != null ? average : 0.0);
    }

    @GetMapping("/student/{studentId}/completed-count")
    @Operation(summary = "완료 강좌 수", description = "특정 학생의 완료한 강좌 수를 조회합니다")
    public ResponseEntity<Long> getCompletedCount(
            @Parameter(description = "학생 ID") @PathVariable Long studentId) {
        return ResponseEntity.ok(progressUseCase.countCompletedByStudentId(studentId));
    }

    // Content Progress APIs
    @PostMapping("/{progressId}/contents")
    @Operation(summary = "콘텐츠 진도 생성", description = "학습 진도에 콘텐츠 진도를 추가합니다")
    public ResponseEntity<ContentProgressResponse> createContentProgress(
            @Parameter(description = "학습 진도 ID") @PathVariable Long progressId,
            @Valid @RequestBody CreateContentProgressRequest request) {
        log.info("Creating content progress for learning: {}, content: {}", progressId, request.getContentId());

        var contentProgress = contentProgressUseCase.createContentProgress(
                progressId,
                request.getContentId(),
                request.getContentType(),
                request.getTotalTimeSeconds());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ContentProgressResponse.from(contentProgress));
    }

    @GetMapping("/{progressId}/contents")
    @Operation(summary = "콘텐츠 진도 목록", description = "학습 진도의 모든 콘텐츠 진도를 조회합니다")
    public ResponseEntity<List<ContentProgressResponse>> getContentProgressList(
            @Parameter(description = "학습 진도 ID") @PathVariable Long progressId) {
        List<ContentProgressResponse> contents = contentProgressUseCase.findByLearningProgressId(progressId)
                .stream()
                .map(ContentProgressResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(contents);
    }

    @PostMapping("/contents/{contentProgressId}/start")
    @Operation(summary = "콘텐츠 시작", description = "콘텐츠 학습을 시작합니다")
    public ResponseEntity<ContentProgressResponse> startContent(
            @Parameter(description = "콘텐츠 진도 ID") @PathVariable Long contentProgressId) {
        var contentProgress = contentProgressUseCase.startContent(contentProgressId);
        return ResponseEntity.ok(ContentProgressResponse.from(contentProgress));
    }

    @PutMapping("/contents/{contentProgressId}")
    @Operation(summary = "콘텐츠 진도 업데이트", description = "콘텐츠 시청 시간을 업데이트합니다")
    public ResponseEntity<ContentProgressResponse> updateContentProgress(
            @Parameter(description = "콘텐츠 진도 ID") @PathVariable Long contentProgressId,
            @Parameter(description = "시청 시간(초)") @RequestParam Long watchedTimeSeconds) {
        var contentProgress = contentProgressUseCase.updateContentProgress(contentProgressId, watchedTimeSeconds);
        return ResponseEntity.ok(ContentProgressResponse.from(contentProgress));
    }

    @PostMapping("/contents/{contentProgressId}/complete")
    @Operation(summary = "콘텐츠 완료", description = "콘텐츠를 완료 처리합니다")
    public ResponseEntity<ContentProgressResponse> completeContent(
            @Parameter(description = "콘텐츠 진도 ID") @PathVariable Long contentProgressId) {
        var contentProgress = contentProgressUseCase.markContentComplete(contentProgressId);
        return ResponseEntity.ok(ContentProgressResponse.from(contentProgress));
    }
}

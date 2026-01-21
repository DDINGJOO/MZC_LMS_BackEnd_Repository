package com.mzc.lms.progress.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LearningProgress {
    private Long id;
    private Long studentId;
    private Long courseId;
    private Long enrollmentId;
    private Integer totalContents;
    private Integer completedContents;
    private Double progressPercentage;
    private Long totalLearningTimeSeconds;
    private ProgressStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LearningProgress create(Long studentId, Long courseId, Long enrollmentId, Integer totalContents) {
        return LearningProgress.builder()
                .studentId(studentId)
                .courseId(courseId)
                .enrollmentId(enrollmentId)
                .totalContents(totalContents)
                .completedContents(0)
                .progressPercentage(0.0)
                .totalLearningTimeSeconds(0L)
                .status(ProgressStatus.NOT_STARTED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public LearningProgress start() {
        return LearningProgress.builder()
                .id(this.id)
                .studentId(this.studentId)
                .courseId(this.courseId)
                .enrollmentId(this.enrollmentId)
                .totalContents(this.totalContents)
                .completedContents(this.completedContents)
                .progressPercentage(this.progressPercentage)
                .totalLearningTimeSeconds(this.totalLearningTimeSeconds)
                .status(ProgressStatus.IN_PROGRESS)
                .startedAt(LocalDateTime.now())
                .lastAccessedAt(LocalDateTime.now())
                .completedAt(this.completedAt)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public LearningProgress updateProgress(Integer completedContents, Long additionalTimeSeconds) {
        int newCompletedContents = Math.min(completedContents, this.totalContents);
        double newProgressPercentage = this.totalContents > 0
                ? (double) newCompletedContents / this.totalContents * 100
                : 0.0;
        ProgressStatus newStatus = newCompletedContents >= this.totalContents
                ? ProgressStatus.COMPLETED
                : ProgressStatus.IN_PROGRESS;
        LocalDateTime newCompletedAt = newStatus == ProgressStatus.COMPLETED
                ? LocalDateTime.now()
                : this.completedAt;

        return LearningProgress.builder()
                .id(this.id)
                .studentId(this.studentId)
                .courseId(this.courseId)
                .enrollmentId(this.enrollmentId)
                .totalContents(this.totalContents)
                .completedContents(newCompletedContents)
                .progressPercentage(newProgressPercentage)
                .totalLearningTimeSeconds(this.totalLearningTimeSeconds + additionalTimeSeconds)
                .status(newStatus)
                .startedAt(this.startedAt)
                .lastAccessedAt(LocalDateTime.now())
                .completedAt(newCompletedAt)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public LearningProgress addLearningTime(Long seconds) {
        return LearningProgress.builder()
                .id(this.id)
                .studentId(this.studentId)
                .courseId(this.courseId)
                .enrollmentId(this.enrollmentId)
                .totalContents(this.totalContents)
                .completedContents(this.completedContents)
                .progressPercentage(this.progressPercentage)
                .totalLearningTimeSeconds(this.totalLearningTimeSeconds + seconds)
                .status(this.status)
                .startedAt(this.startedAt)
                .lastAccessedAt(LocalDateTime.now())
                .completedAt(this.completedAt)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public LearningProgress pause() {
        if (this.status != ProgressStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only pause in-progress learning");
        }
        return LearningProgress.builder()
                .id(this.id)
                .studentId(this.studentId)
                .courseId(this.courseId)
                .enrollmentId(this.enrollmentId)
                .totalContents(this.totalContents)
                .completedContents(this.completedContents)
                .progressPercentage(this.progressPercentage)
                .totalLearningTimeSeconds(this.totalLearningTimeSeconds)
                .status(ProgressStatus.PAUSED)
                .startedAt(this.startedAt)
                .lastAccessedAt(this.lastAccessedAt)
                .completedAt(this.completedAt)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public LearningProgress resume() {
        if (this.status != ProgressStatus.PAUSED) {
            throw new IllegalStateException("Can only resume paused learning");
        }
        return LearningProgress.builder()
                .id(this.id)
                .studentId(this.studentId)
                .courseId(this.courseId)
                .enrollmentId(this.enrollmentId)
                .totalContents(this.totalContents)
                .completedContents(this.completedContents)
                .progressPercentage(this.progressPercentage)
                .totalLearningTimeSeconds(this.totalLearningTimeSeconds)
                .status(ProgressStatus.IN_PROGRESS)
                .startedAt(this.startedAt)
                .lastAccessedAt(LocalDateTime.now())
                .completedAt(this.completedAt)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}

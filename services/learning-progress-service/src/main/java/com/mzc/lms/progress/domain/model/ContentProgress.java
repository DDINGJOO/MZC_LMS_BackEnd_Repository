package com.mzc.lms.progress.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ContentProgress {
    private Long id;
    private Long learningProgressId;
    private Long contentId;
    private String contentType;
    private Double progressPercentage;
    private Long watchedTimeSeconds;
    private Long totalTimeSeconds;
    private ProgressStatus status;
    private Integer viewCount;
    private LocalDateTime startedAt;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ContentProgress create(Long learningProgressId, Long contentId, String contentType, Long totalTimeSeconds) {
        return ContentProgress.builder()
                .learningProgressId(learningProgressId)
                .contentId(contentId)
                .contentType(contentType)
                .progressPercentage(0.0)
                .watchedTimeSeconds(0L)
                .totalTimeSeconds(totalTimeSeconds)
                .status(ProgressStatus.NOT_STARTED)
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ContentProgress start() {
        return ContentProgress.builder()
                .id(this.id)
                .learningProgressId(this.learningProgressId)
                .contentId(this.contentId)
                .contentType(this.contentType)
                .progressPercentage(this.progressPercentage)
                .watchedTimeSeconds(this.watchedTimeSeconds)
                .totalTimeSeconds(this.totalTimeSeconds)
                .status(ProgressStatus.IN_PROGRESS)
                .viewCount(this.viewCount + 1)
                .startedAt(this.startedAt == null ? LocalDateTime.now() : this.startedAt)
                .lastAccessedAt(LocalDateTime.now())
                .completedAt(this.completedAt)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ContentProgress updateProgress(Long watchedTimeSeconds) {
        long newWatchedTime = Math.min(watchedTimeSeconds, this.totalTimeSeconds);
        double newProgressPercentage = this.totalTimeSeconds > 0
                ? (double) newWatchedTime / this.totalTimeSeconds * 100
                : 0.0;
        ProgressStatus newStatus = newProgressPercentage >= 100
                ? ProgressStatus.COMPLETED
                : ProgressStatus.IN_PROGRESS;
        LocalDateTime newCompletedAt = newStatus == ProgressStatus.COMPLETED && this.completedAt == null
                ? LocalDateTime.now()
                : this.completedAt;

        return ContentProgress.builder()
                .id(this.id)
                .learningProgressId(this.learningProgressId)
                .contentId(this.contentId)
                .contentType(this.contentType)
                .progressPercentage(newProgressPercentage)
                .watchedTimeSeconds(newWatchedTime)
                .totalTimeSeconds(this.totalTimeSeconds)
                .status(newStatus)
                .viewCount(this.viewCount)
                .startedAt(this.startedAt)
                .lastAccessedAt(LocalDateTime.now())
                .completedAt(newCompletedAt)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ContentProgress markComplete() {
        return ContentProgress.builder()
                .id(this.id)
                .learningProgressId(this.learningProgressId)
                .contentId(this.contentId)
                .contentType(this.contentType)
                .progressPercentage(100.0)
                .watchedTimeSeconds(this.totalTimeSeconds)
                .totalTimeSeconds(this.totalTimeSeconds)
                .status(ProgressStatus.COMPLETED)
                .viewCount(this.viewCount)
                .startedAt(this.startedAt)
                .lastAccessedAt(LocalDateTime.now())
                .completedAt(LocalDateTime.now())
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public boolean isCompleted() {
        return this.status == ProgressStatus.COMPLETED;
    }
}

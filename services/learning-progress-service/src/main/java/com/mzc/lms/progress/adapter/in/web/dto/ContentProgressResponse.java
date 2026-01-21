package com.mzc.lms.progress.adapter.in.web.dto;

import com.mzc.lms.progress.domain.model.ContentProgress;
import com.mzc.lms.progress.domain.model.ProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentProgressResponse {

    private Long id;
    private Long learningProgressId;
    private Long contentId;
    private String contentType;
    private Double progressPercentage;
    private Long watchedTimeSeconds;
    private Long totalTimeSeconds;
    private ProgressStatus status;
    private String statusDescription;
    private Integer viewCount;
    private LocalDateTime startedAt;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ContentProgressResponse from(ContentProgress progress) {
        return ContentProgressResponse.builder()
                .id(progress.getId())
                .learningProgressId(progress.getLearningProgressId())
                .contentId(progress.getContentId())
                .contentType(progress.getContentType())
                .progressPercentage(progress.getProgressPercentage())
                .watchedTimeSeconds(progress.getWatchedTimeSeconds())
                .totalTimeSeconds(progress.getTotalTimeSeconds())
                .status(progress.getStatus())
                .statusDescription(progress.getStatus().getDescription())
                .viewCount(progress.getViewCount())
                .startedAt(progress.getStartedAt())
                .lastAccessedAt(progress.getLastAccessedAt())
                .completedAt(progress.getCompletedAt())
                .createdAt(progress.getCreatedAt())
                .updatedAt(progress.getUpdatedAt())
                .build();
    }
}

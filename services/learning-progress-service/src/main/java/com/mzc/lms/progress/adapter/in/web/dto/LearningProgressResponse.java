package com.mzc.lms.progress.adapter.in.web.dto;

import com.mzc.lms.progress.domain.model.LearningProgress;
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
public class LearningProgressResponse {

    private Long id;
    private Long studentId;
    private Long courseId;
    private Long enrollmentId;
    private Integer totalContents;
    private Integer completedContents;
    private Double progressPercentage;
    private Long totalLearningTimeSeconds;
    private ProgressStatus status;
    private String statusDescription;
    private LocalDateTime startedAt;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LearningProgressResponse from(LearningProgress progress) {
        return LearningProgressResponse.builder()
                .id(progress.getId())
                .studentId(progress.getStudentId())
                .courseId(progress.getCourseId())
                .enrollmentId(progress.getEnrollmentId())
                .totalContents(progress.getTotalContents())
                .completedContents(progress.getCompletedContents())
                .progressPercentage(progress.getProgressPercentage())
                .totalLearningTimeSeconds(progress.getTotalLearningTimeSeconds())
                .status(progress.getStatus())
                .statusDescription(progress.getStatus().getDescription())
                .startedAt(progress.getStartedAt())
                .lastAccessedAt(progress.getLastAccessedAt())
                .completedAt(progress.getCompletedAt())
                .createdAt(progress.getCreatedAt())
                .updatedAt(progress.getUpdatedAt())
                .build();
    }
}

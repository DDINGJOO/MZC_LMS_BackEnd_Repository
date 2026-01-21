package com.mzc.lms.progress.adapter.out.persistence.entity;

import com.mzc.lms.progress.domain.model.ContentProgress;
import com.mzc.lms.progress.domain.model.ProgressStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "content_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"learning_progress_id", "content_id"}),
        indexes = {
                @Index(name = "idx_content_progress_learning", columnList = "learning_progress_id"),
                @Index(name = "idx_content_progress_content", columnList = "content_id"),
                @Index(name = "idx_content_progress_status", columnList = "status")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentProgressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "learning_progress_id", nullable = false)
    private Long learningProgressId;

    @Column(name = "content_id", nullable = false)
    private Long contentId;

    @Column(name = "content_type", length = 50)
    private String contentType;

    @Column(name = "progress_percentage", nullable = false)
    private Double progressPercentage;

    @Column(name = "watched_time_seconds", nullable = false)
    private Long watchedTimeSeconds;

    @Column(name = "total_time_seconds", nullable = false)
    private Long totalTimeSeconds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProgressStatus status;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static ContentProgressEntity fromDomain(ContentProgress progress) {
        return ContentProgressEntity.builder()
                .id(progress.getId())
                .learningProgressId(progress.getLearningProgressId())
                .contentId(progress.getContentId())
                .contentType(progress.getContentType())
                .progressPercentage(progress.getProgressPercentage())
                .watchedTimeSeconds(progress.getWatchedTimeSeconds())
                .totalTimeSeconds(progress.getTotalTimeSeconds())
                .status(progress.getStatus())
                .viewCount(progress.getViewCount())
                .startedAt(progress.getStartedAt())
                .lastAccessedAt(progress.getLastAccessedAt())
                .completedAt(progress.getCompletedAt())
                .createdAt(progress.getCreatedAt())
                .updatedAt(progress.getUpdatedAt())
                .build();
    }

    public ContentProgress toDomain() {
        return ContentProgress.builder()
                .id(this.id)
                .learningProgressId(this.learningProgressId)
                .contentId(this.contentId)
                .contentType(this.contentType)
                .progressPercentage(this.progressPercentage)
                .watchedTimeSeconds(this.watchedTimeSeconds)
                .totalTimeSeconds(this.totalTimeSeconds)
                .status(this.status)
                .viewCount(this.viewCount)
                .startedAt(this.startedAt)
                .lastAccessedAt(this.lastAccessedAt)
                .completedAt(this.completedAt)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}

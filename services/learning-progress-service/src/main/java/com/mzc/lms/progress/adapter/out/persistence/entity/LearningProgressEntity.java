package com.mzc.lms.progress.adapter.out.persistence.entity;

import com.mzc.lms.progress.domain.model.LearningProgress;
import com.mzc.lms.progress.domain.model.ProgressStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "learning_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}),
        indexes = {
                @Index(name = "idx_progress_student", columnList = "student_id"),
                @Index(name = "idx_progress_course", columnList = "course_id"),
                @Index(name = "idx_progress_status", columnList = "status"),
                @Index(name = "idx_progress_enrollment", columnList = "enrollment_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningProgressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "enrollment_id", nullable = false)
    private Long enrollmentId;

    @Column(name = "total_contents", nullable = false)
    private Integer totalContents;

    @Column(name = "completed_contents", nullable = false)
    private Integer completedContents;

    @Column(name = "progress_percentage", nullable = false)
    private Double progressPercentage;

    @Column(name = "total_learning_time_seconds", nullable = false)
    private Long totalLearningTimeSeconds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProgressStatus status;

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

    public static LearningProgressEntity fromDomain(LearningProgress progress) {
        return LearningProgressEntity.builder()
                .id(progress.getId())
                .studentId(progress.getStudentId())
                .courseId(progress.getCourseId())
                .enrollmentId(progress.getEnrollmentId())
                .totalContents(progress.getTotalContents())
                .completedContents(progress.getCompletedContents())
                .progressPercentage(progress.getProgressPercentage())
                .totalLearningTimeSeconds(progress.getTotalLearningTimeSeconds())
                .status(progress.getStatus())
                .startedAt(progress.getStartedAt())
                .lastAccessedAt(progress.getLastAccessedAt())
                .completedAt(progress.getCompletedAt())
                .createdAt(progress.getCreatedAt())
                .updatedAt(progress.getUpdatedAt())
                .build();
    }

    public LearningProgress toDomain() {
        return LearningProgress.builder()
                .id(this.id)
                .studentId(this.studentId)
                .courseId(this.courseId)
                .enrollmentId(this.enrollmentId)
                .totalContents(this.totalContents)
                .completedContents(this.completedContents)
                .progressPercentage(this.progressPercentage)
                .totalLearningTimeSeconds(this.totalLearningTimeSeconds)
                .status(this.status)
                .startedAt(this.startedAt)
                .lastAccessedAt(this.lastAccessedAt)
                .completedAt(this.completedAt)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}

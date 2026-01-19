package com.mzc.backend.lms.domains.attendance.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 학생 콘텐츠 진행 상황 엔티티
 * MSA 전환을 위해 다른 도메인 Entity 직접 참조 대신 ID만 저장
 */
@Entity
@Table(name = "student_content_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"content_id", "student_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StudentContentProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_id", nullable = false)
    private Long contentId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;

    @Column(name = "progress_percentage", nullable = false)
    private Integer progressPercentage;

    @Column(name = "last_position_seconds")
    private Integer lastPositionSeconds;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "first_accessed_at")
    private LocalDateTime firstAccessedAt;

    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;

    @Column(name = "access_count", nullable = false)
    private Integer accessCount;

    public boolean isVideoCompleted() {
        return Boolean.TRUE.equals(this.isCompleted);
    }
}

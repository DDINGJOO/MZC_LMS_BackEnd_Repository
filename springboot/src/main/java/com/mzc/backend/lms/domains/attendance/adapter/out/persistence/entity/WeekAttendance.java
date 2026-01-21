package com.mzc.backend.lms.domains.attendance.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 주차별 출석 엔티티
 * MSA 전환을 위해 다른 도메인 Entity 직접 참조 대신 ID만 저장
 */
@Entity
@Table(name = "week_attendance",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "week_id"}),
        indexes = {
            @Index(name = "idx_week_attendance_student", columnList = "student_id"),
            @Index(name = "idx_week_attendance_course", columnList = "course_id"),
            @Index(name = "idx_week_attendance_week", columnList = "week_id"),
            @Index(name = "idx_week_attendance_student_course", columnList = "student_id, course_id")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WeekAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "week_id", nullable = false)
    private Long weekId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "is_completed", nullable = false)
    @Builder.Default
    private Boolean isCompleted = false;

    @Column(name = "completed_video_count", nullable = false)
    @Builder.Default
    private Integer completedVideoCount = 0;

    @Column(name = "total_video_count", nullable = false)
    private Integer totalVideoCount;

    @Column(name = "first_accessed_at")
    private LocalDateTime firstAccessedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /**
     * 출석 레코드 생성 팩토리 메서드
     */
    public static WeekAttendance create(Long studentId, Long weekId, Long courseId, int totalVideoCount) {
        return WeekAttendance.builder()
                .studentId(studentId)
                .weekId(weekId)
                .courseId(courseId)
                .isCompleted(false)
                .completedVideoCount(0)
                .totalVideoCount(totalVideoCount)
                .firstAccessedAt(LocalDateTime.now())
                .build();
    }

    /**
     * VIDEO 완료 시 진행 상황 업데이트
     */
    public void updateProgress(int completedVideoCount) {
        if (Boolean.TRUE.equals(this.isCompleted)) {
            return;
        }

        this.completedVideoCount = completedVideoCount;

        if (completedVideoCount >= this.totalVideoCount) {
            markAsCompleted();
        }
    }

    private void markAsCompleted() {
        this.isCompleted = true;
        this.completedAt = LocalDateTime.now();
    }

    public boolean isAttendanceCompleted() {
        return Boolean.TRUE.equals(this.isCompleted);
    }

    public int getProgressPercentage() {
        if (this.totalVideoCount == 0) {
            return 100;
        }
        return (int) ((this.completedVideoCount * 100.0) / this.totalVideoCount);
    }
}

package com.mzc.lms.enrollment.adapter.out.persistence.entity;

import com.mzc.lms.enrollment.domain.model.Enrollment;
import com.mzc.lms.enrollment.domain.model.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}),
        indexes = {
                @Index(name = "idx_enrollment_student", columnList = "student_id"),
                @Index(name = "idx_enrollment_course", columnList = "course_id"),
                @Index(name = "idx_enrollment_status", columnList = "status"),
                @Index(name = "idx_enrollment_student_status", columnList = "student_id, status"),
                @Index(name = "idx_enrollment_course_status", columnList = "course_id, status")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnrollmentStatus status;

    @Column(name = "enrolled_at")
    private LocalDateTime enrolledAt;

    @Column(name = "withdrawn_at")
    private LocalDateTime withdrawnAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "withdrawal_reason", length = 500)
    private String withdrawalReason;

    @Column
    private Integer grade;

    @Column(name = "grade_point", length = 10)
    private String gradePoint;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static EnrollmentEntity fromDomain(Enrollment enrollment) {
        return EnrollmentEntity.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudentId())
                .courseId(enrollment.getCourseId())
                .status(enrollment.getStatus())
                .enrolledAt(enrollment.getEnrolledAt())
                .withdrawnAt(enrollment.getWithdrawnAt())
                .completedAt(enrollment.getCompletedAt())
                .withdrawalReason(enrollment.getWithdrawalReason())
                .grade(enrollment.getGrade())
                .gradePoint(enrollment.getGradePoint())
                .createdAt(enrollment.getCreatedAt())
                .updatedAt(enrollment.getUpdatedAt())
                .build();
    }

    public Enrollment toDomain() {
        return Enrollment.builder()
                .id(this.id)
                .studentId(this.studentId)
                .courseId(this.courseId)
                .status(this.status)
                .enrolledAt(this.enrolledAt)
                .withdrawnAt(this.withdrawnAt)
                .completedAt(this.completedAt)
                .withdrawalReason(this.withdrawalReason)
                .grade(this.grade)
                .gradePoint(this.gradePoint)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}

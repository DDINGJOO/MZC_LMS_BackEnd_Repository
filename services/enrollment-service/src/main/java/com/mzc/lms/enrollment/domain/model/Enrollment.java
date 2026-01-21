package com.mzc.lms.enrollment.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Enrollment {
    private Long id;
    private Long studentId;
    private Long courseId;
    private EnrollmentStatus status;
    private LocalDateTime enrolledAt;
    private LocalDateTime withdrawnAt;
    private LocalDateTime completedAt;
    private String withdrawalReason;
    private Integer grade;
    private String gradePoint;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Enrollment create(Long studentId, Long courseId) {
        return Enrollment.builder()
                .studentId(studentId)
                .courseId(courseId)
                .status(EnrollmentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Enrollment enroll() {
        if (this.status != EnrollmentStatus.PENDING && this.status != EnrollmentStatus.WAITLISTED) {
            throw new IllegalStateException("Only pending or waitlisted enrollments can be enrolled");
        }
        return Enrollment.builder()
                .id(this.id)
                .studentId(this.studentId)
                .courseId(this.courseId)
                .status(EnrollmentStatus.ENROLLED)
                .enrolledAt(LocalDateTime.now())
                .withdrawnAt(this.withdrawnAt)
                .completedAt(this.completedAt)
                .withdrawalReason(this.withdrawalReason)
                .grade(this.grade)
                .gradePoint(this.gradePoint)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Enrollment waitlist() {
        if (this.status != EnrollmentStatus.PENDING) {
            throw new IllegalStateException("Only pending enrollments can be waitlisted");
        }
        return Enrollment.builder()
                .id(this.id)
                .studentId(this.studentId)
                .courseId(this.courseId)
                .status(EnrollmentStatus.WAITLISTED)
                .enrolledAt(this.enrolledAt)
                .withdrawnAt(this.withdrawnAt)
                .completedAt(this.completedAt)
                .withdrawalReason(this.withdrawalReason)
                .grade(this.grade)
                .gradePoint(this.gradePoint)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Enrollment withdraw(String reason) {
        if (!this.status.canWithdraw()) {
            throw new IllegalStateException("Cannot withdraw from enrollment with status: " + this.status);
        }
        return Enrollment.builder()
                .id(this.id)
                .studentId(this.studentId)
                .courseId(this.courseId)
                .status(EnrollmentStatus.WITHDRAWN)
                .enrolledAt(this.enrolledAt)
                .withdrawnAt(LocalDateTime.now())
                .completedAt(this.completedAt)
                .withdrawalReason(reason)
                .grade(this.grade)
                .gradePoint(this.gradePoint)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Enrollment complete(Integer grade, String gradePoint) {
        if (!this.status.canComplete()) {
            throw new IllegalStateException("Only enrolled students can complete the course");
        }
        EnrollmentStatus newStatus = (grade != null && grade >= 60)
                ? EnrollmentStatus.COMPLETED
                : EnrollmentStatus.FAILED;

        return Enrollment.builder()
                .id(this.id)
                .studentId(this.studentId)
                .courseId(this.courseId)
                .status(newStatus)
                .enrolledAt(this.enrolledAt)
                .withdrawnAt(this.withdrawnAt)
                .completedAt(LocalDateTime.now())
                .withdrawalReason(this.withdrawalReason)
                .grade(grade)
                .gradePoint(gradePoint)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Enrollment cancel() {
        if (this.status != EnrollmentStatus.PENDING && this.status != EnrollmentStatus.WAITLISTED) {
            throw new IllegalStateException("Only pending or waitlisted enrollments can be cancelled");
        }
        return Enrollment.builder()
                .id(this.id)
                .studentId(this.studentId)
                .courseId(this.courseId)
                .status(EnrollmentStatus.CANCELLED)
                .enrolledAt(this.enrolledAt)
                .withdrawnAt(LocalDateTime.now())
                .completedAt(this.completedAt)
                .withdrawalReason("Cancelled by user")
                .grade(this.grade)
                .gradePoint(this.gradePoint)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}

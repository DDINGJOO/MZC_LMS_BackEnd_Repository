package com.mzc.lms.enrollment.adapter.in.web.dto;

import com.mzc.lms.enrollment.domain.model.Enrollment;
import com.mzc.lms.enrollment.domain.model.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {

    private Long id;
    private Long studentId;
    private Long courseId;
    private EnrollmentStatus status;
    private String statusDescription;
    private LocalDateTime enrolledAt;
    private LocalDateTime withdrawnAt;
    private LocalDateTime completedAt;
    private String withdrawalReason;
    private Integer grade;
    private String gradePoint;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static EnrollmentResponse from(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudentId())
                .courseId(enrollment.getCourseId())
                .status(enrollment.getStatus())
                .statusDescription(enrollment.getStatus().getDescription())
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
}

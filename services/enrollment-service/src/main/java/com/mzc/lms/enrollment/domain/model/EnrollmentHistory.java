package com.mzc.lms.enrollment.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EnrollmentHistory {
    private Long id;
    private Long enrollmentId;
    private EnrollmentStatus previousStatus;
    private EnrollmentStatus newStatus;
    private String changedBy;
    private String changeReason;
    private LocalDateTime changedAt;

    public static EnrollmentHistory create(
            Long enrollmentId,
            EnrollmentStatus previousStatus,
            EnrollmentStatus newStatus,
            String changedBy,
            String changeReason) {
        return EnrollmentHistory.builder()
                .enrollmentId(enrollmentId)
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .changedBy(changedBy)
                .changeReason(changeReason)
                .changedAt(LocalDateTime.now())
                .build();
    }
}

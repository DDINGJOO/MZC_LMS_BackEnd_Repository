package com.mzc.lms.enrollment.adapter.in.web.dto;

import com.mzc.lms.enrollment.domain.model.EnrollmentHistory;
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
public class EnrollmentHistoryResponse {

    private Long id;
    private Long enrollmentId;
    private EnrollmentStatus previousStatus;
    private EnrollmentStatus newStatus;
    private String changedBy;
    private String changeReason;
    private LocalDateTime changedAt;

    public static EnrollmentHistoryResponse from(EnrollmentHistory history) {
        return EnrollmentHistoryResponse.builder()
                .id(history.getId())
                .enrollmentId(history.getEnrollmentId())
                .previousStatus(history.getPreviousStatus())
                .newStatus(history.getNewStatus())
                .changedBy(history.getChangedBy())
                .changeReason(history.getChangeReason())
                .changedAt(history.getChangedAt())
                .build();
    }
}

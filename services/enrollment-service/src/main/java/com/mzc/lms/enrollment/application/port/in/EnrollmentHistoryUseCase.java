package com.mzc.lms.enrollment.application.port.in;

import com.mzc.lms.enrollment.domain.model.EnrollmentHistory;
import com.mzc.lms.enrollment.domain.model.EnrollmentStatus;

import java.util.List;

public interface EnrollmentHistoryUseCase {

    EnrollmentHistory recordHistory(
            Long enrollmentId,
            EnrollmentStatus previousStatus,
            EnrollmentStatus newStatus,
            String changedBy,
            String changeReason);

    List<EnrollmentHistory> findByEnrollmentId(Long enrollmentId);
}

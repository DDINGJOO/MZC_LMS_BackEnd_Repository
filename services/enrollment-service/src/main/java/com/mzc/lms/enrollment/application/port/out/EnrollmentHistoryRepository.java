package com.mzc.lms.enrollment.application.port.out;

import com.mzc.lms.enrollment.domain.model.EnrollmentHistory;

import java.util.List;

public interface EnrollmentHistoryRepository {

    EnrollmentHistory save(EnrollmentHistory history);

    List<EnrollmentHistory> findByEnrollmentId(Long enrollmentId);

    List<EnrollmentHistory> findByEnrollmentIdOrderByChangedAtDesc(Long enrollmentId);
}

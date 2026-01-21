package com.mzc.lms.enrollment.application.service;

import com.mzc.lms.enrollment.application.port.in.EnrollmentHistoryUseCase;
import com.mzc.lms.enrollment.application.port.out.EnrollmentHistoryRepository;
import com.mzc.lms.enrollment.domain.model.EnrollmentHistory;
import com.mzc.lms.enrollment.domain.model.EnrollmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentHistoryService implements EnrollmentHistoryUseCase {

    private final EnrollmentHistoryRepository historyRepository;

    @Override
    @CacheEvict(value = "enrollment-history", key = "#enrollmentId")
    public EnrollmentHistory recordHistory(
            Long enrollmentId,
            EnrollmentStatus previousStatus,
            EnrollmentStatus newStatus,
            String changedBy,
            String changeReason) {
        log.info("Recording enrollment history for enrollment: {}, {} -> {}",
                enrollmentId, previousStatus, newStatus);

        EnrollmentHistory history = EnrollmentHistory.create(
                enrollmentId, previousStatus, newStatus, changedBy, changeReason);

        EnrollmentHistory saved = historyRepository.save(history);

        log.info("Enrollment history recorded with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "enrollment-history", key = "#enrollmentId")
    public List<EnrollmentHistory> findByEnrollmentId(Long enrollmentId) {
        return historyRepository.findByEnrollmentIdOrderByChangedAtDesc(enrollmentId);
    }
}

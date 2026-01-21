package com.mzc.lms.enrollment.adapter.out.persistence;

import com.mzc.lms.enrollment.adapter.out.persistence.entity.EnrollmentHistoryEntity;
import com.mzc.lms.enrollment.adapter.out.persistence.repository.EnrollmentHistoryJpaRepository;
import com.mzc.lms.enrollment.application.port.out.EnrollmentHistoryRepository;
import com.mzc.lms.enrollment.domain.model.EnrollmentHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EnrollmentHistoryPersistenceAdapter implements EnrollmentHistoryRepository {

    private final EnrollmentHistoryJpaRepository jpaRepository;

    @Override
    public EnrollmentHistory save(EnrollmentHistory history) {
        EnrollmentHistoryEntity entity = EnrollmentHistoryEntity.fromDomain(history);
        EnrollmentHistoryEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public List<EnrollmentHistory> findByEnrollmentId(Long enrollmentId) {
        return jpaRepository.findByEnrollmentId(enrollmentId)
                .stream()
                .map(EnrollmentHistoryEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentHistory> findByEnrollmentIdOrderByChangedAtDesc(Long enrollmentId) {
        return jpaRepository.findByEnrollmentIdOrderByChangedAtDesc(enrollmentId)
                .stream()
                .map(EnrollmentHistoryEntity::toDomain)
                .collect(Collectors.toList());
    }
}

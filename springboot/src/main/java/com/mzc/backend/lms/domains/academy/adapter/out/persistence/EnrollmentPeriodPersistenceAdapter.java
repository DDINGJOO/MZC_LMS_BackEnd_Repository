package com.mzc.backend.lms.domains.academy.adapter.out.persistence;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.mapper.EnrollmentPeriodMapper;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.repository.EnrollmentPeriodJpaRepository;
import com.mzc.backend.lms.domains.academy.application.port.out.EnrollmentPeriodRepositoryPort;
import com.mzc.backend.lms.domains.academy.domain.model.EnrollmentPeriodDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 수강신청 기간 Persistence Adapter
 */
@Component
@RequiredArgsConstructor
public class EnrollmentPeriodPersistenceAdapter implements EnrollmentPeriodRepositoryPort {

    private final EnrollmentPeriodJpaRepository enrollmentPeriodJpaRepository;

    @Override
    public Optional<EnrollmentPeriodDomain> findById(Long id) {
        return enrollmentPeriodJpaRepository.findById(id)
                .map(EnrollmentPeriodMapper::toDomain);
    }

    @Override
    public Optional<EnrollmentPeriodDomain> findByAcademicTermIdAndPeriodName(Long academicTermId, String periodName) {
        return enrollmentPeriodJpaRepository.findByAcademicTermIdAndPeriodName(academicTermId, periodName)
                .map(EnrollmentPeriodMapper::toDomain);
    }

    @Override
    public boolean existsByAcademicTermIdAndPeriodName(Long academicTermId, String periodName) {
        return enrollmentPeriodJpaRepository.existsByAcademicTermIdAndPeriodName(academicTermId, periodName);
    }

    @Override
    public List<EnrollmentPeriodDomain> findByAcademicTermId(Long academicTermId) {
        return enrollmentPeriodJpaRepository.findByAcademicTermId(academicTermId).stream()
                .map(EnrollmentPeriodMapper::toDomain)
                .toList();
    }

    @Override
    public List<EnrollmentPeriodDomain> findByPeriodTypeId(Integer periodTypeId) {
        return enrollmentPeriodJpaRepository.findByPeriodTypeId(periodTypeId).stream()
                .map(EnrollmentPeriodMapper::toDomain)
                .toList();
    }

    @Override
    public List<EnrollmentPeriodDomain> findByPeriodTypeIdAndAcademicTermId(Integer periodTypeId, Long academicTermId) {
        return enrollmentPeriodJpaRepository.findByPeriodTypeIdAndAcademicTermId(periodTypeId, academicTermId).stream()
                .map(EnrollmentPeriodMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<EnrollmentPeriodDomain> findFirstActiveEnrollmentPeriod(LocalDateTime now) {
        return enrollmentPeriodJpaRepository.findFirstActiveEnrollmentPeriod(now)
                .map(EnrollmentPeriodMapper::toDomain);
    }

    @Override
    public Optional<EnrollmentPeriodDomain> findFirstActiveCourseRegistrationPeriod(LocalDateTime now) {
        return enrollmentPeriodJpaRepository.findFirstActiveCourseRegistrationPeriod(now)
                .map(EnrollmentPeriodMapper::toDomain);
    }

    @Override
    public Optional<EnrollmentPeriodDomain> findFirstActivePeriodByTypeCode(String typeCode, LocalDateTime now) {
        return enrollmentPeriodJpaRepository.findFirstActivePeriodByTypeCode(typeCode, now)
                .map(EnrollmentPeriodMapper::toDomain);
    }

    @Override
    public boolean existsActiveEnrollmentPeriod(LocalDateTime now) {
        return enrollmentPeriodJpaRepository.existsActiveEnrollmentPeriod(now);
    }

    @Override
    public boolean existsActiveCourseRegistrationPeriod(LocalDateTime now) {
        return enrollmentPeriodJpaRepository.existsActiveCourseRegistrationPeriod(now);
    }

    @Override
    public Optional<EnrollmentPeriodDomain> findFirstActivePeriod(LocalDateTime now) {
        return enrollmentPeriodJpaRepository.findFirstActivePeriod(now)
                .map(EnrollmentPeriodMapper::toDomain);
    }

    @Override
    public boolean existsActivePeriodByTypeCodeAndAcademicTermId(String typeCode, Long academicTermId, LocalDateTime now) {
        return enrollmentPeriodJpaRepository.existsActivePeriodByTypeCodeAndAcademicTermId(typeCode, academicTermId, now);
    }

    @Override
    public boolean existsActiveCourseRegistrationPeriodByAcademicTermId(Long academicTermId, LocalDateTime now) {
        return enrollmentPeriodJpaRepository.existsActiveCourseRegistrationPeriodByAcademicTermId(academicTermId, now);
    }

    @Override
    public EnrollmentPeriodDomain save(EnrollmentPeriodDomain enrollmentPeriod) {
        throw new UnsupportedOperationException("EnrollmentPeriod save operation requires Entity conversion - not yet implemented");
    }
}

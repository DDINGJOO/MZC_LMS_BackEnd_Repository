package com.mzc.backend.lms.domains.academy.adapter.out.persistence;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.EnrollmentPeriod;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.PeriodType;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.repository.EnrollmentPeriodJpaRepository;
import com.mzc.backend.lms.domains.academy.application.port.out.EnrollmentPeriodRepositoryPort;
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
    public Optional<EnrollmentPeriod> findById(Long id) {
        return enrollmentPeriodJpaRepository.findById(id);
    }

    @Override
    public Optional<EnrollmentPeriod> findByAcademicTermAndPeriodName(AcademicTerm academicTerm, String periodName) {
        return enrollmentPeriodJpaRepository.findByAcademicTermAndPeriodName(academicTerm, periodName);
    }

    @Override
    public boolean existsByAcademicTermAndPeriodName(AcademicTerm academicTerm, String periodName) {
        return enrollmentPeriodJpaRepository.existsByAcademicTermAndPeriodName(academicTerm, periodName);
    }

    @Override
    public List<EnrollmentPeriod> findByAcademicTerm(AcademicTerm academicTerm) {
        return enrollmentPeriodJpaRepository.findByAcademicTerm(academicTerm);
    }

    @Override
    public List<EnrollmentPeriod> findByPeriodType(PeriodType periodType) {
        return enrollmentPeriodJpaRepository.findByPeriodType(periodType);
    }

    @Override
    public List<EnrollmentPeriod> findByPeriodTypeAndAcademicTerm(PeriodType periodType, AcademicTerm academicTerm) {
        return enrollmentPeriodJpaRepository.findByPeriodTypeAndAcademicTerm(periodType, academicTerm);
    }

    @Override
    public Optional<EnrollmentPeriod> findFirstActiveEnrollmentPeriod(LocalDateTime now) {
        return enrollmentPeriodJpaRepository.findFirstActiveEnrollmentPeriod(now);
    }

    @Override
    public Optional<EnrollmentPeriod> findFirstActiveCourseRegistrationPeriod(LocalDateTime now) {
        return enrollmentPeriodJpaRepository.findFirstActiveCourseRegistrationPeriod(now);
    }

    @Override
    public Optional<EnrollmentPeriod> findFirstActivePeriodByTypeCode(String typeCode, LocalDateTime now) {
        return enrollmentPeriodJpaRepository.findFirstActivePeriodByTypeCode(typeCode, now);
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
    public Optional<EnrollmentPeriod> findFirstActivePeriod(LocalDateTime now) {
        return enrollmentPeriodJpaRepository.findFirstActivePeriod(now);
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
    public EnrollmentPeriod save(EnrollmentPeriod enrollmentPeriod) {
        return enrollmentPeriodJpaRepository.save(enrollmentPeriod);
    }
}

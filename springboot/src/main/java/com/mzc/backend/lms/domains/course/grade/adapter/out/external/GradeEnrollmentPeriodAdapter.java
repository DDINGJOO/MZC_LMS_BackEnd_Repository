package com.mzc.backend.lms.domains.course.grade.adapter.out.external;

import com.mzc.backend.lms.domains.course.grade.application.port.out.GradeEnrollmentPeriodPort;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.EnrollmentPeriod;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.repository.EnrollmentPeriodJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 수강신청 기간 외부 Adapter (academy 도메인) - grade 전용
 */
@Component
@RequiredArgsConstructor
public class GradeEnrollmentPeriodAdapter implements GradeEnrollmentPeriodPort {

    private final EnrollmentPeriodJpaRepository enrollmentPeriodRepository;

    @Override
    public Optional<EnrollmentPeriod> findFirstActivePeriodByTypeCode(String typeCode, LocalDateTime now) {
        return enrollmentPeriodRepository.findFirstActivePeriodByTypeCode(typeCode, now);
    }

    @Override
    public boolean existsActivePeriodByTypeCodeAndAcademicTermId(String typeCode, Long academicTermId, LocalDateTime now) {
        return enrollmentPeriodRepository.existsActivePeriodByTypeCodeAndAcademicTermId(typeCode, academicTermId, now);
    }
}

package com.mzc.backend.lms.domains.course.course.adapter.out.external;

import com.mzc.backend.lms.domains.course.course.application.port.out.EnrollmentPeriodPort;
import com.mzc.backend.lms.domains.academy.entity.EnrollmentPeriod;
import com.mzc.backend.lms.domains.academy.repository.EnrollmentPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 수강신청 기간 외부 Adapter (academy 도메인)
 */
@Component("courseEnrollmentPeriodAdapter")
@RequiredArgsConstructor
public class EnrollmentPeriodAdapter implements EnrollmentPeriodPort {

    private final EnrollmentPeriodRepository enrollmentPeriodRepository;

    @Override
    public Optional<EnrollmentPeriod> findById(Long id) {
        return enrollmentPeriodRepository.findById(id);
    }

    @Override
    public boolean existsActiveCourseRegistrationPeriod(LocalDateTime dateTime) {
        return enrollmentPeriodRepository.existsActiveCourseRegistrationPeriod(dateTime);
    }
}

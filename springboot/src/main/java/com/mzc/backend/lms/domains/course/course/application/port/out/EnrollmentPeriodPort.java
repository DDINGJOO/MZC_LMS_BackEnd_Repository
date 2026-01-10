package com.mzc.backend.lms.domains.course.course.application.port.out;

import com.mzc.backend.lms.domains.academy.entity.EnrollmentPeriod;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 수강신청 기간 외부 Port (academy 도메인)
 */
public interface EnrollmentPeriodPort {

    /**
     * ID로 수강신청 기간 조회
     */
    Optional<EnrollmentPeriod> findById(Long id);

    /**
     * 강의 등록 기간 활성화 여부 확인
     */
    boolean existsActiveCourseRegistrationPeriod(LocalDateTime dateTime);
}

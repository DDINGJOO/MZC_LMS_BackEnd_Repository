package com.mzc.backend.lms.domains.course.grade.application.port.out;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.EnrollmentPeriod;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 수강신청 기간 외부 Port (academy 도메인) - grade 전용
 */
public interface GradeEnrollmentPeriodPort {

    /**
     * 타입 코드로 첫 번째 활성화된 기간 조회
     */
    Optional<EnrollmentPeriod> findFirstActivePeriodByTypeCode(String typeCode, LocalDateTime now);

    /**
     * 타입 코드와 학기 ID로 활성화된 기간 존재 여부 확인
     */
    boolean existsActivePeriodByTypeCodeAndAcademicTermId(String typeCode, Long academicTermId, LocalDateTime now);
}

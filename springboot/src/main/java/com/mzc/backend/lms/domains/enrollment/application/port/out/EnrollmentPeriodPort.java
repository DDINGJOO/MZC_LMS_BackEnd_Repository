package com.mzc.backend.lms.domains.enrollment.application.port.out;

import java.time.LocalDateTime;

/**
 * 수강신청 기간(Academy 도메인)과의 통신을 위한 Port
 * MSA 전환 시 HTTP Client로 교체 가능
 */
public interface EnrollmentPeriodPort {

    /**
     * 현재 수강신청 기간 활성화 여부
     */
    boolean isEnrollmentPeriodActive();

    /**
     * 수강신청 취소 가능 기간 여부
     */
    boolean isCancelPeriodActive();

    /**
     * 수강신청 기간 정보 조회
     */
    PeriodInfo getPeriod(Long periodId);

    /**
     * 현재 활성화된 기간 조회
     */
    PeriodInfo getCurrentActivePeriod();

    // DTO Records

    record PeriodInfo(
            Long id,
            String periodName,
            String periodType,
            LocalDateTime startDatetime,
            LocalDateTime endDatetime,
            Long academicTermId,
            int year,
            String termType
    ) {
        public boolean isActive(LocalDateTime now) {
            return !now.isBefore(startDatetime) && !now.isAfter(endDatetime);
        }
    }
}

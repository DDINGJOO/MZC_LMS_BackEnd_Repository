package com.mzc.backend.lms.domains.enrollment.application.port.out;

import java.time.LocalDateTime;
import java.util.Optional;

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

    /**
     * 현재 활성화된 기간 조회 (Optional)
     */
    Optional<PeriodInfo> findCurrentActivePeriod();

    /**
     * 타입 코드로 현재 활성화된 기간 조회
     */
    Optional<PeriodInfo> findCurrentActivePeriodByTypeCode(String typeCode);

    /**
     * 타입 코드 유효성 검증
     */
    boolean isPeriodTypeValid(String typeCode);

    // DTO Records

    record PeriodInfo(
            Long id,
            String periodName,
            String periodTypeCode,
            String periodTypeName,
            String periodTypeDescription,
            LocalDateTime startDatetime,
            LocalDateTime endDatetime,
            Long academicTermId,
            int year,
            String termType,
            Integer targetYear
    ) {
        public boolean isActive(LocalDateTime now) {
            return !now.isBefore(startDatetime) && !now.isAfter(endDatetime);
        }
    }
}

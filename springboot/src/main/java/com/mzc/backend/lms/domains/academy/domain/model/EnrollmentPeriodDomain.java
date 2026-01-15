package com.mzc.backend.lms.domains.academy.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 수강신청 기간 도메인 모델
 */
@Getter
@Builder
public class EnrollmentPeriodDomain {

    private final Long id;
    private final Long academicTermId;
    private final String periodName;
    private final PeriodTypeDomain periodType;
    private final LocalDateTime startDatetime;
    private final LocalDateTime endDatetime;
    private final Integer targetYear;
    private final LocalDateTime createdAt;

    /**
     * 현재 시간이 기간 내에 있는지 확인
     */
    public boolean isActive(LocalDateTime now) {
        return !now.isBefore(startDatetime) && !now.isAfter(endDatetime);
    }

    /**
     * 수강신청 기간인지 확인
     */
    public boolean isEnrollmentPeriod() {
        return periodType != null && periodType.isEnrollmentType();
    }

    /**
     * 강의 등록 기간인지 확인
     */
    public boolean isCourseRegistrationPeriod() {
        return periodType != null && periodType.isCourseRegistrationType();
    }

    /**
     * 정정 기간인지 확인
     */
    public boolean isAdjustmentPeriod() {
        return periodType != null && periodType.isAdjustmentType();
    }

    /**
     * 수강철회 기간인지 확인
     */
    public boolean isCancellationPeriod() {
        return periodType != null && periodType.isCancellationType();
    }
}

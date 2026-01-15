package com.mzc.backend.lms.domains.academy.domain.model;

import lombok.Builder;
import lombok.Getter;

/**
 * 기간 타입 도메인 모델
 */
@Getter
@Builder
public class PeriodTypeDomain {

    private final Integer id;
    private final String typeCode;
    private final String typeName;
    private final String description;

    /**
     * 수강신청 기간 타입인지 확인
     */
    public boolean isEnrollmentType() {
        return "ENROLLMENT".equals(typeCode);
    }

    /**
     * 강의등록 기간 타입인지 확인
     */
    public boolean isCourseRegistrationType() {
        return "COURSE_REGISTRATION".equals(typeCode);
    }

    /**
     * 정정 기간 타입인지 확인
     */
    public boolean isAdjustmentType() {
        return "ADJUSTMENT".equals(typeCode);
    }

    /**
     * 수강철회 기간 타입인지 확인
     */
    public boolean isCancellationType() {
        return "CANCELLATION".equals(typeCode);
    }
}

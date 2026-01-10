package com.mzc.backend.lms.domains.course.grade.application.port.out;

/**
 * 기간 유형 외부 Port (academy 도메인)
 */
public interface PeriodTypePort {

    /**
     * 기간 유형 코드 존재 여부 확인
     */
    boolean existsByTypeCode(String typeCode);
}

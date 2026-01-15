package com.mzc.backend.lms.domains.academy.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 학기 도메인 모델
 */
@Getter
@Builder
public class AcademicTermDomain {

    private final Long id;
    private final Integer year;
    private final String termType;
    private final LocalDate startDate;
    private final LocalDate endDate;

    /**
     * 현재 날짜가 학기 기간 내에 있는지 확인
     */
    public boolean isCurrentTerm(LocalDate today) {
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }
}

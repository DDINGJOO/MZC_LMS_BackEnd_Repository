package com.mzc.backend.lms.domains.course.grade.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 성적 계산에 필요한 평가 정보 DTO
 * assessment 도메인과의 의존성을 끊기 위해 course.grade 도메인에서 정의
 */
@Getter
@Builder
@AllArgsConstructor
public class AssessmentInfo {

    private final Long id;
    private final BigDecimal totalScore;

    public static AssessmentInfo of(Long id, BigDecimal totalScore) {
        return new AssessmentInfo(id, totalScore);
    }
}

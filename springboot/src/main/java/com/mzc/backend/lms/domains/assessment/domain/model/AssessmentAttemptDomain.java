package com.mzc.backend.lms.domains.assessment.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 응시/결과 도메인 모델
 */
@Getter
@Builder
public class AssessmentAttemptDomain {

    private final Long id;
    private final Long assessmentId;
    private final Long userId;
    private final LocalDateTime startedAt;
    private final LocalDateTime submittedAt;
    private final Boolean isLate;
    private final BigDecimal latePenaltyPoints;
    private final BigDecimal latePenaltyRate;
    private final BigDecimal score;
    private final String grade;
    private final String answerData;
    private final String feedback;
    private final LocalDateTime gradedAt;
    private final Long gradedBy;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;

    /**
     * 삭제 여부 확인
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * 제출 여부 확인
     */
    public boolean isSubmitted() {
        return submittedAt != null;
    }

    /**
     * 채점 완료 여부 확인
     */
    public boolean isGraded() {
        return gradedAt != null;
    }
}

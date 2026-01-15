package com.mzc.backend.lms.domains.assessment.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 시험/퀴즈 도메인 모델
 */
@Getter
@Builder
public class AssessmentDomain {

    private final Long id;
    private final Long postId;
    private final Long courseId;
    private final AssessmentTypeDomain type;
    private final LocalDateTime startAt;
    private final Integer durationMinutes;
    private final BigDecimal totalScore;
    private final Boolean isOnline;
    private final String location;
    private final String instructions;
    private final Integer questionCount;
    private final BigDecimal passingScore;
    private final String questionData;
    private final Long createdBy;
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
     * 종료 시간 계산
     */
    public LocalDateTime endAt() {
        return startAt.plusMinutes(durationMinutes);
    }

    /**
     * 현재 진행 중인지 확인
     */
    public boolean isInProgress(LocalDateTime now) {
        return !now.isBefore(startAt) && !now.isAfter(endAt());
    }

    /**
     * 시작 전인지 확인
     */
    public boolean isBeforeStart(LocalDateTime now) {
        return now.isBefore(startAt);
    }

    /**
     * 종료됐는지 확인
     */
    public boolean isEnded(LocalDateTime now) {
        return now.isAfter(endAt());
    }
}

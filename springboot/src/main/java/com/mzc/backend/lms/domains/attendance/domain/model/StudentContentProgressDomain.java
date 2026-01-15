package com.mzc.backend.lms.domains.attendance.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 학생 콘텐츠 진행 상황 도메인 모델
 */
@Getter
@Builder
public class StudentContentProgressDomain {

    private final Long id;
    private final Long contentId;
    private final Long studentId;
    private final Boolean isCompleted;
    private final Integer progressPercentage;
    private final Integer lastPositionSeconds;
    private final LocalDateTime completedAt;
    private final LocalDateTime firstAccessedAt;
    private final LocalDateTime lastAccessedAt;
    private final Integer accessCount;

    /**
     * 콘텐츠 완료 여부 확인
     */
    public boolean isVideoCompleted() {
        return Boolean.TRUE.equals(this.isCompleted);
    }
}

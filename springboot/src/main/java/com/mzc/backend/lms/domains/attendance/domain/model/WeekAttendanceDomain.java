package com.mzc.backend.lms.domains.attendance.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 주차별 출석 도메인 모델
 */
@Getter
@Builder
public class WeekAttendanceDomain {

    private final Long id;
    private final Long studentId;
    private final Long weekId;
    private final Long courseId;
    private final Boolean isCompleted;
    private final Integer completedVideoCount;
    private final Integer totalVideoCount;
    private final LocalDateTime firstAccessedAt;
    private final LocalDateTime completedAt;

    /**
     * 출석 완료 여부 확인
     */
    public boolean isAttendanceCompleted() {
        return Boolean.TRUE.equals(this.isCompleted);
    }

    /**
     * 진행률 계산 (0 ~ 100)
     */
    public int getProgressPercentage() {
        if (this.totalVideoCount == null || this.totalVideoCount == 0) {
            return 100;
        }
        int completed = this.completedVideoCount != null ? this.completedVideoCount : 0;
        return (int) ((completed * 100.0) / this.totalVideoCount);
    }
}

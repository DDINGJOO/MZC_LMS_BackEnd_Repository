package com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;

/**
 * 수강신청 기간 엔티티
 * enrollment_periods 테이블과 매핑
 */
@Entity
@Table(name = "enrollment_periods")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private AcademicTerm academicTerm;

    @Column(name = "period_name", length = 50, nullable = false)
    private String periodName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_type_id", nullable = false)
    private PeriodType periodType;

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDatetime;

    @Column(name = "target_year", nullable = false)
    private Integer targetYear;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

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
        return periodType != null && "ENROLLMENT".equals(periodType.getTypeCode());
    }

    /**
     * 강의 등록 기간인지 확인
     */
    public boolean isCourseRegistrationPeriod() {
        return periodType != null && "COURSE_REGISTRATION".equals(periodType.getTypeCode());
    }

    /**
     * 정정 기간인지 확인
     */
    public boolean isAdjustmentPeriod() {
        return periodType != null && "ADJUSTMENT".equals(periodType.getTypeCode());
    }

    /**
     * 수강철회 기간인지 확인
     */
    public boolean isCancellationPeriod() {
        return periodType != null && "CANCELLATION".equals(periodType.getTypeCode());
    }
}

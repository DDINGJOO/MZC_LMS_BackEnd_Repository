package com.mzc.backend.lms.domains.enrollment.adapter.out.external;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.EnrollmentPeriod;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.repository.EnrollmentPeriodJpaRepository;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentPeriodPort;
import com.mzc.backend.lms.domains.enrollment.domain.exception.EnrollmentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 수강신청 기간(Academy 도메인) Adapter
 * 현재: Repository 직접 호출
 * MSA 전환 시: HTTP Client로 교체
 */
@Component
@RequiredArgsConstructor
public class EnrollmentPeriodAdapter implements EnrollmentPeriodPort {

    private final EnrollmentPeriodJpaRepository periodRepository;

    @Override
    public boolean isEnrollmentPeriodActive() {
        LocalDateTime now = LocalDateTime.now();
        return periodRepository.existsActiveEnrollmentPeriod(now);
    }

    @Override
    public boolean isCancelPeriodActive() {
        LocalDateTime now = LocalDateTime.now();
        return periodRepository.findAll().stream()
                .anyMatch(period -> {
                    boolean isActive = period.getStartDatetime().isBefore(now)
                            && period.getEndDatetime().isAfter(now);
                    String name = period.getPeriodName();
                    return isActive && (name.contains("수강신청")
                            || name.contains("정정")
                            || name.contains("수강철회"));
                });
    }

    @Override
    public PeriodInfo getPeriod(Long periodId) {
        EnrollmentPeriod period = periodRepository.findById(periodId)
                .orElseThrow(() -> EnrollmentException.periodNotFound(periodId));
        return toPeriodInfo(period);
    }

    @Override
    public PeriodInfo getCurrentActivePeriod() {
        LocalDateTime now = LocalDateTime.now();
        return periodRepository.findAll().stream()
                .filter(period -> {
                    LocalDateTime start = period.getStartDatetime();
                    LocalDateTime end = period.getEndDatetime();
                    return !now.isBefore(start) && !now.isAfter(end);
                })
                .findFirst()
                .map(this::toPeriodInfo)
                .orElseThrow(() -> EnrollmentException.notEnrollmentPeriod());
    }

    private PeriodInfo toPeriodInfo(EnrollmentPeriod period) {
        return new PeriodInfo(
                period.getId(),
                period.getPeriodName(),
                period.getPeriodType() != null ? period.getPeriodType().getTypeName() : null,
                period.getStartDatetime(),
                period.getEndDatetime(),
                period.getAcademicTerm().getId(),
                period.getAcademicTerm().getYear(),
                period.getAcademicTerm().getTermType()
        );
    }
}

package com.mzc.backend.lms.integration.academy;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.EnrollmentPeriod;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.repository.EnrollmentPeriodJpaRepository;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.repository.PeriodTypeJpaRepository;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentPeriodPort;
import com.mzc.backend.lms.domains.enrollment.domain.exception.EnrollmentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Academy → Enrollment 통합 Adapter
 *
 * Enrollment 도메인이 Academy 도메인의 데이터에 접근할 때 사용
 * integration 패키지에 위치하여 도메인 간 순환 의존성 방지
 *
 * MSA 전환 시: HTTP Client로 교체
 */
@Component
@RequiredArgsConstructor
public class AcademyEnrollmentAdapter implements EnrollmentPeriodPort {

    private final EnrollmentPeriodJpaRepository periodRepository;
    private final PeriodTypeJpaRepository periodTypeRepository;

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
        return periodRepository.findFirstActivePeriod(now)
                .map(this::toPeriodInfo)
                .orElseThrow(() -> EnrollmentException.notEnrollmentPeriod());
    }

    @Override
    public Optional<PeriodInfo> findCurrentActivePeriod() {
        LocalDateTime now = LocalDateTime.now();
        return periodRepository.findFirstActivePeriod(now)
                .map(this::toPeriodInfo);
    }

    @Override
    public Optional<PeriodInfo> findCurrentActivePeriodByTypeCode(String typeCode) {
        LocalDateTime now = LocalDateTime.now();
        return periodRepository.findFirstActivePeriodByTypeCode(typeCode.toUpperCase(), now)
                .map(this::toPeriodInfo);
    }

    @Override
    public boolean isPeriodTypeValid(String typeCode) {
        return periodTypeRepository.existsByTypeCode(typeCode.toUpperCase());
    }

    private PeriodInfo toPeriodInfo(EnrollmentPeriod period) {
        var periodType = period.getPeriodType();
        var term = period.getAcademicTerm();

        return new PeriodInfo(
                period.getId(),
                period.getPeriodName(),
                periodType != null ? periodType.getTypeCode() : null,
                periodType != null ? periodType.getTypeName() : null,
                periodType != null ? periodType.getDescription() : null,
                period.getStartDatetime(),
                period.getEndDatetime(),
                term.getId(),
                term.getYear(),
                term.getTermType(),
                period.getTargetYear()
        );
    }
}

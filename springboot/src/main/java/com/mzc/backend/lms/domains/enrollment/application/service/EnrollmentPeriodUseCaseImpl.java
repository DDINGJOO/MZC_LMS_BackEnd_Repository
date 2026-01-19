package com.mzc.backend.lms.domains.enrollment.application.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.EnrollmentPeriodResponseDto;
import com.mzc.backend.lms.domains.enrollment.application.port.in.EnrollmentPeriodUseCase;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentPeriodPort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentPeriodPort.PeriodInfo;
import com.mzc.backend.lms.domains.enrollment.domain.exception.EnrollmentException;

/**
 * 수강신청 기간 조회 UseCase 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentPeriodUseCaseImpl implements EnrollmentPeriodUseCase {

    private final EnrollmentPeriodPort enrollmentPeriodPort;

    @Override
    public EnrollmentPeriodResponseDto getCurrentPeriod(String typeCode) {
        LocalDateTime now = LocalDateTime.now();

        Optional<PeriodInfo> currentPeriodOpt;

        // typeCode가 null이거나 빈 문자열이면 현재 활성화된 기간 중 하나를 반환
        if (typeCode == null || typeCode.trim().isEmpty()) {
            currentPeriodOpt = enrollmentPeriodPort.findCurrentActivePeriod();
        } else {
            String periodTypeCode = typeCode.toUpperCase();

            // 타입 코드 유효성 검증
            if (!enrollmentPeriodPort.isPeriodTypeValid(periodTypeCode)) {
                throw EnrollmentException.invalidPeriodType(periodTypeCode);
            }

            // 현재 활성화된 기간 찾기
            currentPeriodOpt = enrollmentPeriodPort.findCurrentActivePeriodByTypeCode(periodTypeCode);
        }

        // 활성화된 기간이 없으면 null 반환
        if (currentPeriodOpt.isEmpty()) {
            return EnrollmentPeriodResponseDto.builder()
                    .isActive(false)
                    .currentPeriod(null)
                    .build();
        }

        PeriodInfo currentPeriod = currentPeriodOpt.get();

        // 날짜를 LocalDateTime으로 변환 (시작일은 00:00:00, 종료일은 23:59:59)
        LocalDateTime startDateTime = currentPeriod.startDatetime();
        LocalDateTime endDateTime = currentPeriod.endDatetime();

        // 남은 시간 계산
        Duration remaining = Duration.between(now, endDateTime);
        long totalSeconds = remaining.getSeconds();
        long days = totalSeconds / 86400;
        long hours = (totalSeconds % 86400) / 3600;
        long minutes = (totalSeconds % 3600) / 60;

        // 학기 이름 생성
        String termName = String.format("%d학년도 %s", currentPeriod.year(), getTermTypeName(currentPeriod.termType()));

        return EnrollmentPeriodResponseDto.builder()
                .isActive(true)
                .currentPeriod(EnrollmentPeriodResponseDto.CurrentPeriodDto.builder()
                        .id(currentPeriod.id())
                        .term(EnrollmentPeriodResponseDto.TermDto.builder()
                                .termId(currentPeriod.academicTermId())
                                .year(currentPeriod.year())
                                .termType(currentPeriod.termType())
                                .termName(termName)
                                .build())
                        .periodName(currentPeriod.periodName())
                        .periodType(currentPeriod.periodTypeCode() != null ? EnrollmentPeriodResponseDto.PeriodTypeDto.builder()
                                .id(null) // ID는 Port에서 제공하지 않음
                                .typeCode(currentPeriod.periodTypeCode())
                                .typeName(currentPeriod.periodTypeName())
                                .description(currentPeriod.periodTypeDescription())
                                .build() : null)
                        .startDatetime(startDateTime)
                        .endDatetime(endDateTime)
                        .targetYear(currentPeriod.targetYear() == null || currentPeriod.targetYear() == 0 ? null : currentPeriod.targetYear())
                        .remainingTime(EnrollmentPeriodResponseDto.RemainingTimeDto.builder()
                                .days(days > 0 ? days : 0)
                                .hours(hours > 0 ? hours : 0)
                                .minutes(minutes > 0 ? minutes : 0)
                                .totalSeconds(totalSeconds > 0 ? totalSeconds : 0)
                                .build())
                        .build())
                .build();
    }

    /**
     * 학기 타입 이름 변환
     */
    private String getTermTypeName(String termType) {
        return switch (termType) {
            case "1" -> "1학기";
            case "2" -> "2학기";
            case "SUMMER" -> "여름학기";
            case "WINTER" -> "겨울학기";
            default -> termType;
        };
    }

}

package com.mzc.backend.lms.domains.course.grade.adapter.out.external;

import com.mzc.backend.lms.domains.course.grade.application.port.out.PeriodTypePort;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.repository.PeriodTypeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 기간 유형 외부 Adapter (academy 도메인)
 */
@Component
@RequiredArgsConstructor
public class PeriodTypeAdapter implements PeriodTypePort {

    private final PeriodTypeJpaRepository periodTypeRepository;

    @Override
    public boolean existsByTypeCode(String typeCode) {
        return periodTypeRepository.existsByTypeCode(typeCode);
    }
}

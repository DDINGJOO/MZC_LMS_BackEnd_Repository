package com.mzc.backend.lms.domains.academy.adapter.out.persistence.mapper;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.EnrollmentPeriod;
import com.mzc.backend.lms.domains.academy.domain.model.EnrollmentPeriodDomain;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * EnrollmentPeriod Entity <-> Domain 변환 Mapper
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnrollmentPeriodMapper {

    /**
     * Entity -> Domain 변환
     */
    public static EnrollmentPeriodDomain toDomain(EnrollmentPeriod entity) {
        if (entity == null) {
            return null;
        }
        return EnrollmentPeriodDomain.builder()
                .id(entity.getId())
                .academicTermId(entity.getAcademicTerm() != null ? entity.getAcademicTerm().getId() : null)
                .periodName(entity.getPeriodName())
                .periodType(PeriodTypeMapper.toDomain(entity.getPeriodType()))
                .startDatetime(entity.getStartDatetime())
                .endDatetime(entity.getEndDatetime())
                .targetYear(entity.getTargetYear())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}

package com.mzc.backend.lms.domains.academy.adapter.out.persistence.mapper;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;
import com.mzc.backend.lms.domains.academy.domain.model.AcademicTermDomain;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * AcademicTerm Entity <-> Domain 변환 Mapper
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AcademicTermMapper {

    /**
     * Entity -> Domain 변환
     */
    public static AcademicTermDomain toDomain(AcademicTerm entity) {
        if (entity == null) {
            return null;
        }
        return AcademicTermDomain.builder()
                .id(entity.getId())
                .year(entity.getYear())
                .termType(entity.getTermType())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }

    /**
     * Domain -> Entity 변환
     */
    public static AcademicTerm toEntity(AcademicTermDomain domain) {
        if (domain == null) {
            return null;
        }
        return AcademicTerm.builder()
                .id(domain.getId())
                .year(domain.getYear())
                .termType(domain.getTermType())
                .startDate(domain.getStartDate())
                .endDate(domain.getEndDate())
                .build();
    }
}

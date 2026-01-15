package com.mzc.backend.lms.domains.academy.adapter.out.persistence.mapper;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.PeriodType;
import com.mzc.backend.lms.domains.academy.domain.model.PeriodTypeDomain;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * PeriodType Entity <-> Domain 변환 Mapper
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PeriodTypeMapper {

    /**
     * Entity -> Domain 변환
     */
    public static PeriodTypeDomain toDomain(PeriodType entity) {
        if (entity == null) {
            return null;
        }
        return PeriodTypeDomain.builder()
                .id(entity.getId())
                .typeCode(entity.getTypeCode())
                .typeName(entity.getTypeName())
                .description(entity.getDescription())
                .build();
    }

    /**
     * Domain -> Entity 변환
     */
    public static PeriodType toEntity(PeriodTypeDomain domain) {
        if (domain == null) {
            return null;
        }
        return PeriodType.builder()
                .id(domain.getId())
                .typeCode(domain.getTypeCode())
                .typeName(domain.getTypeName())
                .description(domain.getDescription())
                .build();
    }
}

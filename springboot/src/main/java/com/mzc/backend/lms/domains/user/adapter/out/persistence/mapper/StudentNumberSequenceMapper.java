package com.mzc.backend.lms.domains.user.adapter.out.persistence.mapper;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.StudentNumberSequence;
import com.mzc.backend.lms.domains.user.domain.model.StudentNumberSequenceDomain;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * StudentNumberSequence Entity <-> Domain 변환 Mapper
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentNumberSequenceMapper {

    /**
     * Entity -> Domain 변환
     */
    public static StudentNumberSequenceDomain toDomain(StudentNumberSequence entity) {
        if (entity == null) {
            return null;
        }
        return StudentNumberSequenceDomain.builder()
                .id(entity.getId())
                .year(entity.getYear())
                .collegeId(entity.getCollegeId())
                .departmentId(entity.getDepartmentId())
                .lastSequence(entity.getLastSequence())
                .version(entity.getVersion())
                .build();
    }

    /**
     * Domain -> Entity 변환 (신규 생성용)
     */
    public static StudentNumberSequence toEntity(StudentNumberSequenceDomain domain) {
        if (domain == null) {
            return null;
        }
        return StudentNumberSequence.createWithInitialSequence(
                domain.getYear(),
                domain.getCollegeId(),
                domain.getDepartmentId(),
                domain.getLastSequence()
        );
    }
}

package com.mzc.backend.lms.domains.attendance.adapter.out.persistence.mapper;

import com.mzc.backend.lms.domains.attendance.adapter.out.persistence.entity.StudentContentProgress;
import com.mzc.backend.lms.domains.attendance.domain.model.StudentContentProgressDomain;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * StudentContentProgress Entity <-> Domain 변환 Mapper
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentContentProgressMapper {

    /**
     * Entity -> Domain 변환
     */
    public static StudentContentProgressDomain toDomain(StudentContentProgress entity) {
        if (entity == null) {
            return null;
        }
        return StudentContentProgressDomain.builder()
                .id(entity.getId())
                .contentId(entity.getContentId())
                .studentId(entity.getStudentId())
                .isCompleted(entity.getIsCompleted())
                .progressPercentage(entity.getProgressPercentage())
                .lastPositionSeconds(entity.getLastPositionSeconds())
                .completedAt(entity.getCompletedAt())
                .firstAccessedAt(entity.getFirstAccessedAt())
                .lastAccessedAt(entity.getLastAccessedAt())
                .accessCount(entity.getAccessCount())
                .build();
    }
}

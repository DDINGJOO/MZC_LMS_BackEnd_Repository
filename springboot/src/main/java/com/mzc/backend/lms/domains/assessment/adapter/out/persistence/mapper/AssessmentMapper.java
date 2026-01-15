package com.mzc.backend.lms.domains.assessment.adapter.out.persistence.mapper;

import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.Assessment;
import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.enums.AssessmentType;
import com.mzc.backend.lms.domains.assessment.domain.model.AssessmentDomain;
import com.mzc.backend.lms.domains.assessment.domain.model.AssessmentTypeDomain;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Assessment Entity <-> Domain 변환 Mapper
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssessmentMapper {

    /**
     * Entity -> Domain 변환
     */
    public static AssessmentDomain toDomain(Assessment entity) {
        if (entity == null) {
            return null;
        }
        return AssessmentDomain.builder()
                .id(entity.getId())
                .postId(entity.getPost() != null ? entity.getPost().getId() : null)
                .courseId(entity.getCourseId())
                .type(toTypeDomain(entity.getType()))
                .startAt(entity.getStartAt())
                .durationMinutes(entity.getDurationMinutes())
                .totalScore(entity.getTotalScore())
                .isOnline(entity.getIsOnline())
                .location(entity.getLocation())
                .instructions(entity.getInstructions())
                .questionCount(entity.getQuestionCount())
                .passingScore(entity.getPassingScore())
                .questionData(entity.getQuestionData())
                .createdBy(entity.getCreatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    /**
     * AssessmentType Entity enum -> Domain enum 변환
     */
    private static AssessmentTypeDomain toTypeDomain(AssessmentType type) {
        if (type == null) {
            return null;
        }
        return switch (type) {
            case MIDTERM -> AssessmentTypeDomain.MIDTERM;
            case FINAL -> AssessmentTypeDomain.FINAL;
            case QUIZ -> AssessmentTypeDomain.QUIZ;
            case REGULAR -> AssessmentTypeDomain.REGULAR;
        };
    }
}

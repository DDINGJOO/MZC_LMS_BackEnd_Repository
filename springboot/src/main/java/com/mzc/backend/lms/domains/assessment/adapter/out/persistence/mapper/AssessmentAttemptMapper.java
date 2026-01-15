package com.mzc.backend.lms.domains.assessment.adapter.out.persistence.mapper;

import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.AssessmentAttempt;
import com.mzc.backend.lms.domains.assessment.domain.model.AssessmentAttemptDomain;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * AssessmentAttempt Entity <-> Domain 변환 Mapper
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssessmentAttemptMapper {

    /**
     * Entity -> Domain 변환
     */
    public static AssessmentAttemptDomain toDomain(AssessmentAttempt entity) {
        if (entity == null) {
            return null;
        }
        return AssessmentAttemptDomain.builder()
                .id(entity.getId())
                .assessmentId(entity.getAssessment() != null ? entity.getAssessment().getId() : null)
                .userId(entity.getUserId())
                .startedAt(entity.getStartedAt())
                .submittedAt(entity.getSubmittedAt())
                .isLate(entity.getIsLate())
                .latePenaltyPoints(entity.getLatePenaltyPoints())
                .latePenaltyRate(entity.getLatePenaltyRate())
                .score(entity.getScore())
                .grade(entity.getGrade())
                .answerData(entity.getAnswerData())
                .feedback(entity.getFeedback())
                .gradedAt(entity.getGradedAt())
                .gradedBy(entity.getGradedBy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}

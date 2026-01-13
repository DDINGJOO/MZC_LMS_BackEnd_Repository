package com.mzc.backend.lms.domains.course.grade.adapter.out.external;

import com.mzc.backend.lms.domains.course.grade.application.port.out.AssessmentPort;
import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.Assessment;
import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.enums.AssessmentType;
import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.repository.AssessmentJpaRepository;
import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.repository.AssessmentAttemptJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 평가 외부 Adapter (assessment 도메인)
 */
@Component
@RequiredArgsConstructor
public class AssessmentAdapter implements AssessmentPort {

    private final AssessmentJpaRepository assessmentRepository;
    private final AssessmentAttemptJpaRepository assessmentAttemptRepository;

    @Override
    public List<Assessment> findActiveByCourse(Long courseId, AssessmentType type) {
        return assessmentRepository.findActiveByCourse(courseId, type);
    }

    @Override
    public boolean existsUngradedSubmittedByAssessmentIds(List<Long> assessmentIds) {
        return assessmentAttemptRepository.existsUngradedSubmittedByAssessmentIds(assessmentIds);
    }

    @Override
    public BigDecimal sumGradedScoreByUserAndAssessmentIds(Long userId, List<Long> assessmentIds) {
        return assessmentAttemptRepository.sumGradedScoreByUserAndAssessmentIds(userId, assessmentIds);
    }
}

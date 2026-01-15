package com.mzc.backend.lms.domains.assessment.adapter.out.persistence;

import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.Assessment;
import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.enums.AssessmentType;
import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.repository.AssessmentJpaRepository;
import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.repository.AssessmentAttemptJpaRepository;
import com.mzc.backend.lms.domains.course.grade.application.port.out.AssessmentPort;
import com.mzc.backend.lms.domains.course.grade.domain.enums.GradeAssessmentType;
import com.mzc.backend.lms.domains.course.grade.domain.model.AssessmentInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * course.grade 도메인의 AssessmentPort 구현체
 * assessment 도메인이 course.grade에 데이터를 제공하는 Adapter
 */
@Component
@RequiredArgsConstructor
public class CourseGradeAssessmentAdapter implements AssessmentPort {

    private final AssessmentJpaRepository assessmentRepository;
    private final AssessmentAttemptJpaRepository assessmentAttemptRepository;

    @Override
    public List<AssessmentInfo> findActiveByCourse(Long courseId, GradeAssessmentType type) {
        AssessmentType assessmentType = mapToAssessmentType(type);
        return assessmentRepository.findActiveByCourse(courseId, assessmentType).stream()
                .map(this::toAssessmentInfo)
                .toList();
    }

    @Override
    public boolean existsUngradedSubmittedByAssessmentIds(List<Long> assessmentIds) {
        return assessmentAttemptRepository.existsUngradedSubmittedByAssessmentIds(assessmentIds);
    }

    @Override
    public BigDecimal sumGradedScoreByUserAndAssessmentIds(Long userId, List<Long> assessmentIds) {
        return assessmentAttemptRepository.sumGradedScoreByUserAndAssessmentIds(userId, assessmentIds);
    }

    private AssessmentInfo toAssessmentInfo(Assessment assessment) {
        return AssessmentInfo.of(assessment.getId(), assessment.getTotalScore());
    }

    private AssessmentType mapToAssessmentType(GradeAssessmentType type) {
        return switch (type) {
            case QUIZ -> AssessmentType.QUIZ;
            case MIDTERM -> AssessmentType.MIDTERM;
            case FINAL -> AssessmentType.FINAL;
        };
    }
}

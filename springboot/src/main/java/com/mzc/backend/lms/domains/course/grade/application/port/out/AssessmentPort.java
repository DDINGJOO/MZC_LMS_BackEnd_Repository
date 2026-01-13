package com.mzc.backend.lms.domains.course.grade.application.port.out;

import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.Assessment;
import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.enums.AssessmentType;

import java.math.BigDecimal;
import java.util.List;

/**
 * 평가 외부 Port (assessment 도메인)
 */
public interface AssessmentPort {

    /**
     * 강의 ID와 평가 유형으로 활성 평가 목록 조회
     */
    List<Assessment> findActiveByCourse(Long courseId, AssessmentType type);

    /**
     * 평가 ID 목록에 채점 미완료 응시가 있는지 확인
     */
    boolean existsUngradedSubmittedByAssessmentIds(List<Long> assessmentIds);

    /**
     * 사용자 ID와 평가 ID 목록으로 채점된 점수 합계 조회
     */
    BigDecimal sumGradedScoreByUserAndAssessmentIds(Long userId, List<Long> assessmentIds);
}

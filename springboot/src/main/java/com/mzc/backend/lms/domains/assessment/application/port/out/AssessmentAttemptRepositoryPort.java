package com.mzc.backend.lms.domains.assessment.application.port.out;

import com.mzc.backend.lms.domains.assessment.domain.model.AssessmentAttemptDomain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * AssessmentAttempt 영속성을 위한 Port
 */
public interface AssessmentAttemptRepositoryPort {

    /**
     * 응시 저장
     */
    AssessmentAttemptDomain save(AssessmentAttemptDomain attempt);

    /**
     * ID로 응시 조회 (Assessment와 함께)
     */
    Optional<AssessmentAttemptDomain> findActiveWithAssessment(Long id);

    /**
     * 평가 ID와 상태로 응시 목록 조회
     */
    List<AssessmentAttemptDomain> findActiveByAssessmentIdAndStatus(Long assessmentId, String status);

    /**
     * 평가 ID와 사용자 ID로 응시 조회
     */
    Optional<AssessmentAttemptDomain> findActiveByAssessmentIdAndUserId(Long assessmentId, Long userId);

    /**
     * 미채점된 제출 응시가 있는지 확인
     */
    boolean existsUngradedSubmittedByAssessmentIds(List<Long> assessmentIds);

    /**
     * 사용자의 채점된 점수 합계 조회
     */
    BigDecimal sumGradedScoreByUserAndAssessmentIds(Long userId, List<Long> assessmentIds);
}

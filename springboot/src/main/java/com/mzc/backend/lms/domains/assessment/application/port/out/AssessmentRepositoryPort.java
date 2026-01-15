package com.mzc.backend.lms.domains.assessment.application.port.out;

import com.mzc.backend.lms.domains.assessment.domain.model.AssessmentDomain;
import com.mzc.backend.lms.domains.assessment.domain.model.AssessmentTypeDomain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Assessment 영속성을 위한 Port
 */
public interface AssessmentRepositoryPort {

    /**
     * 평가 저장
     */
    AssessmentDomain save(AssessmentDomain assessment);

    /**
     * ID로 평가 조회 (Post와 함께)
     */
    Optional<AssessmentDomain> findActiveWithPost(Long id);

    /**
     * 강의와 타입으로 평가 목록 조회 (교수용)
     */
    List<AssessmentDomain> findActiveByCourse(Long courseId, AssessmentTypeDomain type);

    /**
     * 강의와 타입으로 학생에게 보이는 평가 목록 조회
     */
    List<AssessmentDomain> findVisibleByCourseForStudent(Long courseId, AssessmentTypeDomain type, LocalDateTime now);
}

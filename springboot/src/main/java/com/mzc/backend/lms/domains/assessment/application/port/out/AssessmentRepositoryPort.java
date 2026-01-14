package com.mzc.backend.lms.domains.assessment.application.port.out;

import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.Assessment;
import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.enums.AssessmentType;

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
    Assessment save(Assessment assessment);

    /**
     * ID로 평가 조회 (Post와 함께)
     */
    Optional<Assessment> findActiveWithPost(Long id);

    /**
     * 강의와 타입으로 평가 목록 조회 (교수용)
     */
    List<Assessment> findActiveByCourse(Long courseId, AssessmentType type);

    /**
     * 강의와 타입으로 학생에게 보이는 평가 목록 조회
     */
    List<Assessment> findVisibleByCourseForStudent(Long courseId, AssessmentType type, LocalDateTime now);
}

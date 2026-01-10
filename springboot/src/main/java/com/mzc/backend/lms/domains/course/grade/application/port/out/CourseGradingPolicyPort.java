package com.mzc.backend.lms.domains.course.grade.application.port.out;

import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.CourseGradingPolicy;

import java.util.Optional;

/**
 * 강의 성적 정책 Port
 */
public interface CourseGradingPolicyPort {

    /**
     * 강의 ID로 성적 정책 조회
     */
    Optional<CourseGradingPolicy> findById(Long courseId);
}

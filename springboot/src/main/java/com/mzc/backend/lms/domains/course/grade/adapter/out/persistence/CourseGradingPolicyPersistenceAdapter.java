package com.mzc.backend.lms.domains.course.grade.adapter.out.persistence;

import com.mzc.backend.lms.domains.course.grade.application.port.out.CourseGradingPolicyPort;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.CourseGradingPolicy;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.repository.CourseGradingPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 강의 성적 정책 Adapter
 */
@Component
@RequiredArgsConstructor
public class CourseGradingPolicyPersistenceAdapter implements CourseGradingPolicyPort {

    private final CourseGradingPolicyRepository courseGradingPolicyRepository;

    @Override
    public Optional<CourseGradingPolicy> findById(Long courseId) {
        return courseGradingPolicyRepository.findById(courseId);
    }
}

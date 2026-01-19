package com.mzc.backend.lms.integration.enrollment;

import com.mzc.backend.lms.domains.course.course.application.port.out.EnrollmentPort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Enrollment → Course 통합 Adapter (course.course 도메인용)
 *
 * Course 도메인이 Enrollment 도메인의 데이터에 접근할 때 사용
 * integration 패키지에 위치하여 도메인 간 순환 의존성 방지
 *
 * MSA 전환 시: HTTP Client로 교체
 */
@Component("courseEnrollmentAdapter")
@RequiredArgsConstructor
public class CourseEnrollmentAdapter implements EnrollmentPort {

    private final EnrollmentRepositoryPort enrollmentRepository;

    @Override
    public long countByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).size();
    }

    @Override
    public boolean existsByStudentIdAndCourseId(Long studentId, Long courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }
}

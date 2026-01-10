package com.mzc.backend.lms.domains.course.course.adapter.out.external;

import com.mzc.backend.lms.domains.course.course.application.port.out.EnrollmentPort;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 수강신청 외부 Adapter (enrollment 도메인)
 */
@Component("courseEnrollmentAdapter")
@RequiredArgsConstructor
public class EnrollmentAdapter implements EnrollmentPort {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public long countByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).size();
    }

    @Override
    public boolean existsByStudentIdAndCourseId(Long studentId, Long courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }
}

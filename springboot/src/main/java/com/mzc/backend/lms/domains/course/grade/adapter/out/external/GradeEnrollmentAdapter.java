package com.mzc.backend.lms.domains.course.grade.adapter.out.external;

import com.mzc.backend.lms.domains.course.grade.application.port.out.GradeEnrollmentPort;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity.Enrollment;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 수강신청 외부 Adapter (enrollment 도메인) - grade 전용
 */
@Component
@RequiredArgsConstructor
public class GradeEnrollmentAdapter implements GradeEnrollmentPort {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public List<Enrollment> findByCourseIdWithStudent(Long courseId) {
        return enrollmentRepository.findByCourseIdWithStudent(courseId);
    }
}

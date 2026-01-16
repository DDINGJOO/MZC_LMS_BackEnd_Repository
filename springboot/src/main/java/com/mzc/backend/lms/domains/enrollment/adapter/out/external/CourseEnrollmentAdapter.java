package com.mzc.backend.lms.domains.enrollment.adapter.out.external;

import com.mzc.backend.lms.domains.course.course.application.port.out.EnrollmentPort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 수강신청 외부 Adapter (course.course 도메인용)
 * course.course 도메인의 EnrollmentPort를 구현하여 수강신청 데이터 제공
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

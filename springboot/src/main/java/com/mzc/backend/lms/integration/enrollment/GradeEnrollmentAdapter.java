package com.mzc.backend.lms.integration.enrollment;

import com.mzc.backend.lms.domains.course.grade.application.port.out.GradeEnrollmentPort;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 수강신청 외부 Adapter (course.grade 도메인용)
 * course.grade 도메인의 GradeEnrollmentPort를 구현하여 수강신청 데이터 제공
 */
@Component
@RequiredArgsConstructor
public class GradeEnrollmentAdapter implements GradeEnrollmentPort {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public List<EnrolledStudentInfo> findStudentsByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseIdWithStudent(courseId).stream()
                .map(enrollment -> new EnrolledStudentInfo(
                        enrollment.getStudent().getStudentId(),
                        enrollment.getStudent().getStudentNumber()
                ))
                .toList();
    }
}

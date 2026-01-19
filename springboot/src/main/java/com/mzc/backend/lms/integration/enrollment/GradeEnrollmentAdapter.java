package com.mzc.backend.lms.integration.enrollment;

import com.mzc.backend.lms.domains.course.grade.application.port.out.GradeEnrollmentPort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Enrollment → Course.Grade 통합 Adapter
 *
 * Grade 도메인이 Enrollment 도메인의 데이터에 접근할 때 사용
 * integration 패키지에 위치하여 도메인 간 순환 의존성 방지
 *
 * MSA 전환 시: HTTP Client로 교체
 */
@Component
@RequiredArgsConstructor
public class GradeEnrollmentAdapter implements GradeEnrollmentPort {

    private final EnrollmentRepositoryPort enrollmentRepository;

    @Override
    public List<EnrolledStudentInfo> findStudentsByCourseId(Long courseId) {
        // studentId와 studentNumber는 동일한 값 (Student 엔티티에서 studentId가 학번)
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(enrollment -> new EnrolledStudentInfo(
                        enrollment.getStudentId(),
                        enrollment.getStudentId()  // studentNumber = studentId
                ))
                .toList();
    }
}

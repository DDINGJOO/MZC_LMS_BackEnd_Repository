package com.mzc.backend.lms.integration.enrollment;

import com.mzc.backend.lms.domains.course.notice.application.port.out.NoticeEnrollmentPort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Enrollment → Course.Notice 통합 Adapter
 *
 * Notice 도메인이 Enrollment 도메인의 데이터에 접근할 때 사용
 * integration 패키지에 위치하여 도메인 간 순환 의존성 방지
 *
 * MSA 전환 시: HTTP Client로 교체
 */
@Component
@RequiredArgsConstructor
public class NoticeEnrollmentAdapter implements NoticeEnrollmentPort {

    private final EnrollmentRepositoryPort enrollmentRepository;

    @Override
    public boolean existsByStudentIdAndCourseId(Long studentId, Long courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public List<Long> findStudentIdsByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(enrollment -> enrollment.getStudentId())
                .collect(Collectors.toList());
    }
}

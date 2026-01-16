package com.mzc.backend.lms.integration.enrollment;

import com.mzc.backend.lms.domains.course.notice.application.port.out.NoticeEnrollmentPort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentRepositoryPort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentRepositoryPort.EnrollmentInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 수강신청 외부 Adapter (course.notice 도메인용)
 * course.notice 도메인의 NoticeEnrollmentPort를 구현하여 수강신청 데이터 제공
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
                .map(EnrollmentInfo::studentId)
                .collect(Collectors.toList());
    }
}

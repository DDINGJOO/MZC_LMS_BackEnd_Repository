package com.mzc.backend.lms.domains.course.notice.adapter.out.external;

import com.mzc.backend.lms.domains.course.notice.application.port.out.NoticeEnrollmentPort;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 수강신청 외부 Adapter (enrollment 도메인) - notice 전용
 */
@Component
@RequiredArgsConstructor
public class NoticeEnrollmentAdapter implements NoticeEnrollmentPort {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public boolean existsByStudentIdAndCourseId(Long studentId, Long courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }
}

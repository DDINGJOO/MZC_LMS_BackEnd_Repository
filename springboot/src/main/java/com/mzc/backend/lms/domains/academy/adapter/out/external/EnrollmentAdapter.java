package com.mzc.backend.lms.domains.academy.adapter.out.external;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;
import com.mzc.backend.lms.domains.academy.application.port.out.EnrollmentQueryPort;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Enrollment 도메인 연동 Adapter
 */
@Component
@RequiredArgsConstructor
public class EnrollmentAdapter implements EnrollmentQueryPort {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public List<AcademicTerm> findDistinctAcademicTermsByStudentId(Long studentId) {
        return enrollmentRepository.findDistinctAcademicTermsByStudentId(studentId);
    }
}

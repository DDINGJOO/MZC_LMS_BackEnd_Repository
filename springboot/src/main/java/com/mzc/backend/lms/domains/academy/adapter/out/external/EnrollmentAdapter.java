package com.mzc.backend.lms.domains.academy.adapter.out.external;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.mapper.AcademicTermMapper;
import com.mzc.backend.lms.domains.academy.application.port.out.EnrollmentQueryPort;
import com.mzc.backend.lms.domains.academy.domain.model.AcademicTermDomain;
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
    public List<AcademicTermDomain> findDistinctAcademicTermsByStudentId(Long studentId) {
        return enrollmentRepository.findDistinctAcademicTermsByStudentId(studentId).stream()
                .map(AcademicTermMapper::toDomain)
                .toList();
    }
}

package com.mzc.backend.lms.domains.enrollment.adapter.out.persistence;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.mapper.AcademicTermMapper;
import com.mzc.backend.lms.domains.academy.application.port.out.EnrollmentQueryPort;
import com.mzc.backend.lms.domains.academy.domain.model.AcademicTermDomain;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * academy 도메인의 EnrollmentQueryPort 구현체
 * enrollment 도메인이 academy에 데이터를 제공하는 Adapter
 */
@Component
@RequiredArgsConstructor
public class AcademyEnrollmentQueryAdapter implements EnrollmentQueryPort {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public List<AcademicTermDomain> findDistinctAcademicTermsByStudentId(Long studentId) {
        return enrollmentRepository.findDistinctAcademicTermsByStudentId(studentId).stream()
                .map(AcademicTermMapper::toDomain)
                .toList();
    }
}

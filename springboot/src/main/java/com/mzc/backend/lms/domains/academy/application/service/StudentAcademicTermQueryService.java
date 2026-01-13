package com.mzc.backend.lms.domains.academy.application.service;

import com.mzc.backend.lms.domains.academy.adapter.in.web.dto.AcademicTermResponseDto;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;
import com.mzc.backend.lms.domains.academy.application.port.in.StudentAcademicTermQueryUseCase;
import com.mzc.backend.lms.domains.academy.application.port.out.EnrollmentQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 학생 학기 조회 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentAcademicTermQueryService implements StudentAcademicTermQueryUseCase {

    private final EnrollmentQueryPort enrollmentQueryPort;

    @Override
    public List<AcademicTermResponseDto> getMyAcademicTerms(Long studentId) {
        Objects.requireNonNull(studentId, "studentId");

        List<AcademicTerm> terms = enrollmentQueryPort.findDistinctAcademicTermsByStudentId(studentId);

        return terms.stream()
                .sorted(Comparator.comparing(AcademicTerm::getId, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .map(AcademicTermResponseDto::from)
                .toList();
    }
}

package com.mzc.backend.lms.domains.academy.application.service;

import com.mzc.backend.lms.domains.academy.adapter.in.web.dto.AcademicTermResponseDto;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;
import com.mzc.backend.lms.domains.academy.application.port.in.ProfessorAcademicTermQueryUseCase;
import com.mzc.backend.lms.domains.academy.application.port.out.CourseQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 교수 학기 조회 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfessorAcademicTermQueryService implements ProfessorAcademicTermQueryUseCase {

    private final CourseQueryPort courseQueryPort;

    @Override
    public List<AcademicTermResponseDto> getMyAcademicTerms(Long professorId) {
        Objects.requireNonNull(professorId, "professorId");

        List<AcademicTerm> terms = courseQueryPort.findDistinctAcademicTermsByProfessorId(professorId);

        return terms.stream()
                .sorted(Comparator.comparing(AcademicTerm::getId, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .map(AcademicTermResponseDto::from)
                .toList();
    }
}

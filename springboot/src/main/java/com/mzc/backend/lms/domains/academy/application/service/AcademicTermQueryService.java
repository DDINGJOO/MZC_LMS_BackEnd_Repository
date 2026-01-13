package com.mzc.backend.lms.domains.academy.application.service;

import com.mzc.backend.lms.domains.academy.adapter.in.web.dto.AcademicTermResponseDto;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;
import com.mzc.backend.lms.domains.academy.application.port.in.AcademicTermQueryUseCase;
import com.mzc.backend.lms.domains.academy.application.port.out.AcademicTermRepositoryPort;
import com.mzc.backend.lms.domains.academy.exception.AcademyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * 학기 조회 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AcademicTermQueryService implements AcademicTermQueryUseCase {

    private final AcademicTermRepositoryPort academicTermRepositoryPort;

    @Override
    public AcademicTermResponseDto getCurrentAcademicTerm(LocalDate today) {
        AcademicTerm academicTerm = academicTermRepositoryPort.findCurrentTerms(today).stream()
                .findFirst()
                .orElseThrow(AcademyException::currentSemesterNotSet);

        return AcademicTermResponseDto.from(academicTerm);
    }

    @Override
    public AcademicTermResponseDto getAcademicTermById(Long id) {
        AcademicTerm academicTerm = academicTermRepositoryPort.findById(id)
                .orElseThrow(() -> AcademyException.semesterNotFound(id));

        return AcademicTermResponseDto.from(academicTerm);
    }
}

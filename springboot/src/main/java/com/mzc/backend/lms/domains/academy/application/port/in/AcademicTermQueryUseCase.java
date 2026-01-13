package com.mzc.backend.lms.domains.academy.application.port.in;

import com.mzc.backend.lms.domains.academy.adapter.in.web.dto.AcademicTermResponseDto;

import java.time.LocalDate;

/**
 * 학기 조회 UseCase
 */
public interface AcademicTermQueryUseCase {

    /**
     * 현재 학기 조회
     */
    AcademicTermResponseDto getCurrentAcademicTerm(LocalDate today);

    /**
     * ID로 학기 조회
     */
    AcademicTermResponseDto getAcademicTermById(Long id);
}

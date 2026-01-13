package com.mzc.backend.lms.domains.academy.application.port.in;

import com.mzc.backend.lms.domains.academy.adapter.in.web.dto.AcademicTermResponseDto;

import java.util.List;

/**
 * 교수 학기 조회 UseCase
 */
public interface ProfessorAcademicTermQueryUseCase {

    /**
     * 교수 본인 담당 학기 목록 조회
     */
    List<AcademicTermResponseDto> getMyAcademicTerms(Long professorId);
}

package com.mzc.backend.lms.domains.academy.application.port.in;

import com.mzc.backend.lms.domains.academy.adapter.in.web.dto.AcademicTermResponseDto;

import java.util.List;

/**
 * 학생 학기 조회 UseCase
 */
public interface StudentAcademicTermQueryUseCase {

    /**
     * 학생 본인 수강 학기 목록 조회
     */
    List<AcademicTermResponseDto> getMyAcademicTerms(Long studentId);
}

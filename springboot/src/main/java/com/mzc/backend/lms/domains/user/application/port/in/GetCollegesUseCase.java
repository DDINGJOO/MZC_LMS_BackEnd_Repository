package com.mzc.backend.lms.domains.user.application.port.in;

import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.CollegeListResponseDto;

import java.util.List;

/**
 * 단과대학 목록 조회 UseCase
 */
public interface GetCollegesUseCase {

    /**
     * 전체 단과대학 목록 조회
     * @return 단과대학 목록
     */
    List<CollegeListResponseDto> getAllColleges();
}

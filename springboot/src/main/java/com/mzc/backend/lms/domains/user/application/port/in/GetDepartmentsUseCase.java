package com.mzc.backend.lms.domains.user.application.port.in;

import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.DepartmentListResponseDto;

import java.util.List;

/**
 * 학과 목록 조회 UseCase
 */
public interface GetDepartmentsUseCase {

    /**
     * 전체 학과 목록 조회
     * @return 학과 목록
     */
    List<DepartmentListResponseDto> getAllDepartments();

    /**
     * 단과대학별 학과 목록 조회
     * @param collegeId 단과대학 ID
     * @return 학과 목록
     */
    List<DepartmentListResponseDto> getDepartmentsByCollegeId(Long collegeId);
}

package com.mzc.backend.lms.domains.course.course.application.port.out;

import com.mzc.backend.lms.domains.academy.entity.AcademicTerm;

import java.util.Optional;

/**
 * 학기 외부 Port (academy 도메인)
 */
public interface AcademicTermPort {

    /**
     * ID로 학기 조회
     */
    Optional<AcademicTerm> findById(Long id);
}

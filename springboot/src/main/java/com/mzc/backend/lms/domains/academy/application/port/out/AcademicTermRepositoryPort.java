package com.mzc.backend.lms.domains.academy.application.port.out;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 학기 Repository Port
 */
public interface AcademicTermRepositoryPort {

    /**
     * ID로 학기 조회
     */
    Optional<AcademicTerm> findById(Long id);

    /**
     * 학년도와 학기 구분으로 학기 조회
     */
    Optional<AcademicTerm> findByYearAndTermType(Integer year, String termType);

    /**
     * 학년도와 학기 구분 존재 여부 확인
     */
    boolean existsByYearAndTermType(Integer year, String termType);

    /**
     * 학년도로 학기 목록 조회
     */
    List<AcademicTerm> findByYear(Integer year);

    /**
     * 학기 구분으로 학기 목록 조회
     */
    List<AcademicTerm> findByTermType(String termType);

    /**
     * 현재 날짜에 해당하는 학기 조회
     */
    List<AcademicTerm> findCurrentTerms(LocalDate today);

    /**
     * 학기 저장
     */
    AcademicTerm save(AcademicTerm academicTerm);
}

package com.mzc.backend.lms.domains.academy.application.port.out;

import com.mzc.backend.lms.domains.academy.domain.model.AcademicTermDomain;

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
    Optional<AcademicTermDomain> findById(Long id);

    /**
     * 학년도와 학기 구분으로 학기 조회
     */
    Optional<AcademicTermDomain> findByYearAndTermType(Integer year, String termType);

    /**
     * 학년도와 학기 구분 존재 여부 확인
     */
    boolean existsByYearAndTermType(Integer year, String termType);

    /**
     * 학년도로 학기 목록 조회
     */
    List<AcademicTermDomain> findByYear(Integer year);

    /**
     * 학기 구분으로 학기 목록 조회
     */
    List<AcademicTermDomain> findByTermType(String termType);

    /**
     * 현재 날짜에 해당하는 학기 조회
     */
    List<AcademicTermDomain> findCurrentTerms(LocalDate today);

    /**
     * 학기 저장
     */
    AcademicTermDomain save(AcademicTermDomain academicTerm);
}

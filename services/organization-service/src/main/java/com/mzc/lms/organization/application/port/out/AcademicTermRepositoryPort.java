package com.mzc.lms.organization.application.port.out;

import com.mzc.lms.organization.domain.model.AcademicTerm;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AcademicTermRepositoryPort {

    AcademicTerm save(AcademicTerm academicTerm);

    Optional<AcademicTerm> findById(Long id);

    Optional<AcademicTerm> findCurrentTerm(LocalDate date);

    List<AcademicTerm> findAll();

    List<AcademicTerm> findByYear(Integer year);

    void deleteById(Long id);

    boolean existsByYearAndTermType(Integer year, String termType);
}

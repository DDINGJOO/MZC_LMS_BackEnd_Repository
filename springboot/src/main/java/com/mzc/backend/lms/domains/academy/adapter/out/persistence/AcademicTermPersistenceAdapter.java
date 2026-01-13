package com.mzc.backend.lms.domains.academy.adapter.out.persistence;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.repository.AcademicTermJpaRepository;
import com.mzc.backend.lms.domains.academy.application.port.out.AcademicTermRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 학기 Persistence Adapter
 */
@Component
@RequiredArgsConstructor
public class AcademicTermPersistenceAdapter implements AcademicTermRepositoryPort {

    private final AcademicTermJpaRepository academicTermJpaRepository;

    @Override
    public Optional<AcademicTerm> findById(Long id) {
        return academicTermJpaRepository.findById(id);
    }

    @Override
    public Optional<AcademicTerm> findByYearAndTermType(Integer year, String termType) {
        return academicTermJpaRepository.findByYearAndTermType(year, termType);
    }

    @Override
    public boolean existsByYearAndTermType(Integer year, String termType) {
        return academicTermJpaRepository.existsByYearAndTermType(year, termType);
    }

    @Override
    public List<AcademicTerm> findByYear(Integer year) {
        return academicTermJpaRepository.findByYear(year);
    }

    @Override
    public List<AcademicTerm> findByTermType(String termType) {
        return academicTermJpaRepository.findByTermType(termType);
    }

    @Override
    public List<AcademicTerm> findCurrentTerms(LocalDate today) {
        return academicTermJpaRepository.findCurrentTerms(today);
    }

    @Override
    public AcademicTerm save(AcademicTerm academicTerm) {
        return academicTermJpaRepository.save(academicTerm);
    }
}

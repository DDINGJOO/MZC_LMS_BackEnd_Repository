package com.mzc.backend.lms.domains.academy.adapter.out.persistence;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.mapper.AcademicTermMapper;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.repository.AcademicTermJpaRepository;
import com.mzc.backend.lms.domains.academy.application.port.out.AcademicTermRepositoryPort;
import com.mzc.backend.lms.domains.academy.domain.model.AcademicTermDomain;
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
    public Optional<AcademicTermDomain> findById(Long id) {
        return academicTermJpaRepository.findById(id)
                .map(AcademicTermMapper::toDomain);
    }

    @Override
    public Optional<AcademicTermDomain> findByYearAndTermType(Integer year, String termType) {
        return academicTermJpaRepository.findByYearAndTermType(year, termType)
                .map(AcademicTermMapper::toDomain);
    }

    @Override
    public boolean existsByYearAndTermType(Integer year, String termType) {
        return academicTermJpaRepository.existsByYearAndTermType(year, termType);
    }

    @Override
    public List<AcademicTermDomain> findByYear(Integer year) {
        return academicTermJpaRepository.findByYear(year).stream()
                .map(AcademicTermMapper::toDomain)
                .toList();
    }

    @Override
    public List<AcademicTermDomain> findByTermType(String termType) {
        return academicTermJpaRepository.findByTermType(termType).stream()
                .map(AcademicTermMapper::toDomain)
                .toList();
    }

    @Override
    public List<AcademicTermDomain> findCurrentTerms(LocalDate today) {
        return academicTermJpaRepository.findCurrentTerms(today).stream()
                .map(AcademicTermMapper::toDomain)
                .toList();
    }

    @Override
    public AcademicTermDomain save(AcademicTermDomain academicTerm) {
        var entity = AcademicTermMapper.toEntity(academicTerm);
        var savedEntity = academicTermJpaRepository.save(entity);
        return AcademicTermMapper.toDomain(savedEntity);
    }
}

package com.mzc.lms.organization.adapter.out.persistence;

import com.mzc.lms.organization.adapter.out.persistence.entity.AcademicTermEntity;
import com.mzc.lms.organization.adapter.out.persistence.repository.AcademicTermJpaRepository;
import com.mzc.lms.organization.application.port.out.AcademicTermRepositoryPort;
import com.mzc.lms.organization.domain.model.AcademicTerm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AcademicTermPersistenceAdapter implements AcademicTermRepositoryPort {

    private final AcademicTermJpaRepository repository;

    @Override
    public AcademicTerm save(AcademicTerm academicTerm) {
        AcademicTermEntity entity = academicTerm.getId() != null
                ? repository.findById(academicTerm.getId())
                    .map(existing -> {
                        existing.updatePeriod(academicTerm.getStartDate(), academicTerm.getEndDate());
                        return existing;
                    })
                    .orElseGet(() -> toEntity(academicTerm))
                : toEntity(academicTerm);

        AcademicTermEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<AcademicTerm> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<AcademicTerm> findCurrentTerm(LocalDate date) {
        return repository.findCurrentTerm(date).map(this::toDomain);
    }

    @Override
    public List<AcademicTerm> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AcademicTerm> findByYear(Integer year) {
        return repository.findByYear(year).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByYearAndTermType(Integer year, String termType) {
        return repository.existsByYearAndTermType(year, termType);
    }

    private AcademicTerm toDomain(AcademicTermEntity entity) {
        return AcademicTerm.builder()
                .id(entity.getId())
                .year(entity.getYear())
                .termType(entity.getTermType())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }

    private AcademicTermEntity toEntity(AcademicTerm domain) {
        return AcademicTermEntity.builder()
                .year(domain.getYear())
                .termType(domain.getTermType())
                .startDate(domain.getStartDate())
                .endDate(domain.getEndDate())
                .build();
    }
}

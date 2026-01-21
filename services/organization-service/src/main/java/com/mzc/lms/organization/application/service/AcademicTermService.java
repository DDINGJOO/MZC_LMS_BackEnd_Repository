package com.mzc.lms.organization.application.service;

import com.mzc.lms.organization.adapter.in.web.dto.AcademicTermCreateRequest;
import com.mzc.lms.organization.adapter.in.web.dto.AcademicTermUpdateRequest;
import com.mzc.lms.organization.application.port.in.AcademicTermUseCase;
import com.mzc.lms.organization.application.port.out.AcademicTermRepositoryPort;
import com.mzc.lms.organization.application.port.out.EventPublisherPort;
import com.mzc.lms.organization.domain.event.OrganizationEvent;
import com.mzc.lms.organization.domain.model.AcademicTerm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AcademicTermService implements AcademicTermUseCase {

    private final AcademicTermRepositoryPort academicTermRepository;
    private final EventPublisherPort eventPublisher;

    @Override
    @Transactional
    @CacheEvict(value = "academicTerms", allEntries = true)
    public AcademicTerm createAcademicTerm(AcademicTermCreateRequest request) {
        log.info("Creating academic term: {} {}", request.getYear(), request.getTermType());

        if (academicTermRepository.existsByYearAndTermType(request.getYear(), request.getTermType())) {
            throw new IllegalArgumentException("Academic term already exists: " + request.getYear() + " " + request.getTermType());
        }

        AcademicTerm term = AcademicTerm.builder()
                .year(request.getYear())
                .termType(request.getTermType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        AcademicTerm saved = academicTermRepository.save(term);
        eventPublisher.publish(OrganizationEvent.academicTermCreated(saved.getId(), saved.getTermName()));

        return saved;
    }

    @Override
    @Transactional
    @CacheEvict(value = "academicTerms", allEntries = true)
    public AcademicTerm updateAcademicTerm(Long id, AcademicTermUpdateRequest request) {
        log.info("Updating academic term: {}", id);

        AcademicTerm term = academicTermRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Academic term not found: " + id));

        if (request.getStartDate() != null && request.getEndDate() != null) {
            term.updatePeriod(request.getStartDate(), request.getEndDate());
        }

        return academicTermRepository.save(term);
    }

    @Override
    @Transactional
    @CacheEvict(value = "academicTerms", allEntries = true)
    public void deleteAcademicTerm(Long id) {
        log.info("Deleting academic term: {}", id);
        academicTermRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "academicTerms", key = "#id")
    public AcademicTerm getAcademicTerm(Long id) {
        return academicTermRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Academic term not found: " + id));
    }

    @Override
    @Cacheable(value = "academicTerms", key = "'current'")
    public Optional<AcademicTerm> getCurrentTerm() {
        return academicTermRepository.findCurrentTerm(LocalDate.now());
    }

    @Override
    @Cacheable(value = "academicTerms", key = "'all'")
    public List<AcademicTerm> getAllTerms() {
        return academicTermRepository.findAll();
    }

    @Override
    @Cacheable(value = "academicTerms", key = "'year:' + #year")
    public List<AcademicTerm> getTermsByYear(Integer year) {
        return academicTermRepository.findByYear(year);
    }
}

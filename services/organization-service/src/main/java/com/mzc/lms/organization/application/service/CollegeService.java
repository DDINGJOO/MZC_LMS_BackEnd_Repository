package com.mzc.lms.organization.application.service;

import com.mzc.lms.organization.adapter.in.web.dto.CollegeCreateRequest;
import com.mzc.lms.organization.adapter.in.web.dto.CollegeUpdateRequest;
import com.mzc.lms.organization.application.port.in.CollegeUseCase;
import com.mzc.lms.organization.application.port.out.CollegeRepositoryPort;
import com.mzc.lms.organization.application.port.out.EventPublisherPort;
import com.mzc.lms.organization.domain.event.OrganizationEvent;
import com.mzc.lms.organization.domain.model.College;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CollegeService implements CollegeUseCase {

    private final CollegeRepositoryPort collegeRepository;
    private final EventPublisherPort eventPublisher;

    @Override
    @Transactional
    @CacheEvict(value = "colleges", allEntries = true)
    public College createCollege(CollegeCreateRequest request) {
        log.info("Creating college: {}", request.getCollegeName());

        if (collegeRepository.existsByCode(request.getCollegeCode())) {
            throw new IllegalArgumentException("College code already exists: " + request.getCollegeCode());
        }

        College college = College.builder()
                .collegeCode(request.getCollegeCode())
                .collegeNumberCode(request.getCollegeNumberCode())
                .collegeName(request.getCollegeName())
                .build();

        College saved = collegeRepository.save(college);
        eventPublisher.publish(OrganizationEvent.collegeCreated(saved.getId(), saved.getCollegeName()));

        return saved;
    }

    @Override
    @Transactional
    @CacheEvict(value = "colleges", allEntries = true)
    public College updateCollege(Long id, CollegeUpdateRequest request) {
        log.info("Updating college: {}", id);

        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("College not found: " + id));

        if (request.getCollegeName() != null) {
            college.updateName(request.getCollegeName());
        }
        if (request.getCollegeCode() != null) {
            college.updateCode(request.getCollegeCode());
        }

        return collegeRepository.save(college);
    }

    @Override
    @Transactional
    @CacheEvict(value = "colleges", allEntries = true)
    public void deleteCollege(Long id) {
        log.info("Deleting college: {}", id);
        collegeRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "colleges", key = "#id")
    public College getCollege(Long id) {
        return collegeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("College not found: " + id));
    }

    @Override
    @Cacheable(value = "colleges", key = "'code:' + #code")
    public College getCollegeByCode(String code) {
        return collegeRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("College not found with code: " + code));
    }

    @Override
    @Cacheable(value = "colleges", key = "'all'")
    public List<College> getAllColleges() {
        return collegeRepository.findAll();
    }
}

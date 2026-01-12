package com.mzc.backend.lms.domains.user.adapter.out.persistence;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.College;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.CollegeRepository;
import com.mzc.backend.lms.domains.user.application.port.out.CollegeQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * College 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
public class CollegePersistenceAdapter implements CollegeQueryPort {

    private final CollegeRepository collegeRepository;

    @Override
    public List<College> findAll() {
        return collegeRepository.findAll();
    }

    @Override
    public Optional<College> findById(Long id) {
        return collegeRepository.findById(id);
    }

    @Override
    public Optional<College> findByCollegeCode(String collegeCode) {
        return collegeRepository.findByCollegeCode(collegeCode);
    }
}

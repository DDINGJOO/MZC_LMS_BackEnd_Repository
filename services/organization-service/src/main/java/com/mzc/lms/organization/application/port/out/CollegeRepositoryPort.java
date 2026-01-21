package com.mzc.lms.organization.application.port.out;

import com.mzc.lms.organization.domain.model.College;

import java.util.List;
import java.util.Optional;

public interface CollegeRepositoryPort {

    College save(College college);

    Optional<College> findById(Long id);

    Optional<College> findByCode(String code);

    List<College> findAll();

    void deleteById(Long id);

    boolean existsByCode(String code);
}

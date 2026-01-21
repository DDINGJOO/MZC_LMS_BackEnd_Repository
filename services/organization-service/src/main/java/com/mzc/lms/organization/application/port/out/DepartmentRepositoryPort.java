package com.mzc.lms.organization.application.port.out;

import com.mzc.lms.organization.domain.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepositoryPort {

    Department save(Department department);

    Optional<Department> findById(Long id);

    Optional<Department> findByCode(String code);

    List<Department> findAll();

    List<Department> findByCollegeId(Long collegeId);

    void deleteById(Long id);

    boolean existsByCode(String code);
}

package com.mzc.backend.lms.domains.user.adapter.out.persistence;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Department;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.DepartmentRepository;
import com.mzc.backend.lms.domains.user.application.port.out.DepartmentQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Department 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
public class DepartmentPersistenceAdapter implements DepartmentQueryPort {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public List<Department> findByCollegeId(Long collegeId) {
        return departmentRepository.findByCollegeId(collegeId);
    }

    @Override
    public Optional<Department> findByDepartmentCode(String departmentCode) {
        return departmentRepository.findByDepartmentCode(departmentCode);
    }
}

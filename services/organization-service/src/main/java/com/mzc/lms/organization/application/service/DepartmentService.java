package com.mzc.lms.organization.application.service;

import com.mzc.lms.organization.adapter.in.web.dto.DepartmentCreateRequest;
import com.mzc.lms.organization.adapter.in.web.dto.DepartmentUpdateRequest;
import com.mzc.lms.organization.application.port.in.DepartmentUseCase;
import com.mzc.lms.organization.application.port.out.CollegeRepositoryPort;
import com.mzc.lms.organization.application.port.out.DepartmentRepositoryPort;
import com.mzc.lms.organization.application.port.out.EventPublisherPort;
import com.mzc.lms.organization.domain.event.OrganizationEvent;
import com.mzc.lms.organization.domain.model.College;
import com.mzc.lms.organization.domain.model.Department;
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
public class DepartmentService implements DepartmentUseCase {

    private final DepartmentRepositoryPort departmentRepository;
    private final CollegeRepositoryPort collegeRepository;
    private final EventPublisherPort eventPublisher;

    @Override
    @Transactional
    @CacheEvict(value = "departments", allEntries = true)
    public Department createDepartment(DepartmentCreateRequest request) {
        log.info("Creating department: {}", request.getDepartmentName());

        if (departmentRepository.existsByCode(request.getDepartmentCode())) {
            throw new IllegalArgumentException("Department code already exists: " + request.getDepartmentCode());
        }

        College college = collegeRepository.findById(request.getCollegeId())
                .orElseThrow(() -> new IllegalArgumentException("College not found: " + request.getCollegeId()));

        Department department = Department.builder()
                .departmentCode(request.getDepartmentCode())
                .departmentName(request.getDepartmentName())
                .collegeId(college.getId())
                .collegeName(college.getCollegeName())
                .build();

        Department saved = departmentRepository.save(department);
        eventPublisher.publish(OrganizationEvent.departmentCreated(saved.getId(), saved.getDepartmentName()));

        return saved;
    }

    @Override
    @Transactional
    @CacheEvict(value = "departments", allEntries = true)
    public Department updateDepartment(Long id, DepartmentUpdateRequest request) {
        log.info("Updating department: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + id));

        if (request.getDepartmentName() != null) {
            department.updateName(request.getDepartmentName());
        }
        if (request.getDepartmentCode() != null) {
            department.updateCode(request.getDepartmentCode());
        }
        if (request.getCollegeId() != null) {
            department.changeCollege(request.getCollegeId());
        }

        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    @CacheEvict(value = "departments", allEntries = true)
    public void deleteDepartment(Long id) {
        log.info("Deleting department: {}", id);
        departmentRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "departments", key = "#id")
    public Department getDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + id));
    }

    @Override
    @Cacheable(value = "departments", key = "'code:' + #code")
    public Department getDepartmentByCode(String code) {
        return departmentRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with code: " + code));
    }

    @Override
    @Cacheable(value = "departments", key = "'all'")
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    @Cacheable(value = "departments", key = "'college:' + #collegeId")
    public List<Department> getDepartmentsByCollege(Long collegeId) {
        return departmentRepository.findByCollegeId(collegeId);
    }
}

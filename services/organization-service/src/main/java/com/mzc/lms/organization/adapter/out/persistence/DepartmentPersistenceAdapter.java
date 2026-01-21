package com.mzc.lms.organization.adapter.out.persistence;

import com.mzc.lms.organization.adapter.out.persistence.entity.CollegeEntity;
import com.mzc.lms.organization.adapter.out.persistence.entity.DepartmentEntity;
import com.mzc.lms.organization.adapter.out.persistence.repository.CollegeJpaRepository;
import com.mzc.lms.organization.adapter.out.persistence.repository.DepartmentJpaRepository;
import com.mzc.lms.organization.application.port.out.DepartmentRepositoryPort;
import com.mzc.lms.organization.domain.model.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DepartmentPersistenceAdapter implements DepartmentRepositoryPort {

    private final DepartmentJpaRepository repository;
    private final CollegeJpaRepository collegeRepository;

    @Override
    public Department save(Department department) {
        CollegeEntity college = collegeRepository.findById(department.getCollegeId())
                .orElseThrow(() -> new IllegalArgumentException("College not found: " + department.getCollegeId()));

        DepartmentEntity entity = department.getId() != null
                ? repository.findById(department.getId())
                    .map(existing -> {
                        existing.updateName(department.getDepartmentName());
                        existing.updateCode(department.getDepartmentCode());
                        if (!existing.getCollege().getId().equals(department.getCollegeId())) {
                            existing.changeCollege(college);
                        }
                        return existing;
                    })
                    .orElseGet(() -> toEntity(department, college))
                : toEntity(department, college);

        DepartmentEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Department> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Department> findByCode(String code) {
        return repository.findByDepartmentCode(code).map(this::toDomain);
    }

    @Override
    public List<Department> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Department> findByCollegeId(Long collegeId) {
        return repository.findByCollegeId(collegeId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return repository.existsByDepartmentCode(code);
    }

    private Department toDomain(DepartmentEntity entity) {
        return Department.builder()
                .id(entity.getId())
                .departmentCode(entity.getDepartmentCode())
                .departmentName(entity.getDepartmentName())
                .collegeId(entity.getCollege().getId())
                .collegeName(entity.getCollege().getCollegeName())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private DepartmentEntity toEntity(Department domain, CollegeEntity college) {
        return DepartmentEntity.builder()
                .departmentCode(domain.getDepartmentCode())
                .departmentName(domain.getDepartmentName())
                .college(college)
                .build();
    }
}

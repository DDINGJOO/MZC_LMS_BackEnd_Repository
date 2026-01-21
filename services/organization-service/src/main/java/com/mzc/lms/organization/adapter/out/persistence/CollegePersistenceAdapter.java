package com.mzc.lms.organization.adapter.out.persistence;

import com.mzc.lms.organization.adapter.out.persistence.entity.CollegeEntity;
import com.mzc.lms.organization.adapter.out.persistence.repository.CollegeJpaRepository;
import com.mzc.lms.organization.application.port.out.CollegeRepositoryPort;
import com.mzc.lms.organization.domain.model.College;
import com.mzc.lms.organization.domain.model.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CollegePersistenceAdapter implements CollegeRepositoryPort {

    private final CollegeJpaRepository repository;

    @Override
    public College save(College college) {
        CollegeEntity entity = college.getId() != null
                ? repository.findById(college.getId())
                    .map(existing -> {
                        existing.updateName(college.getCollegeName());
                        existing.updateCode(college.getCollegeCode());
                        return existing;
                    })
                    .orElseGet(() -> toEntity(college))
                : toEntity(college);

        CollegeEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<College> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<College> findByCode(String code) {
        return repository.findByCollegeCode(code).map(this::toDomain);
    }

    @Override
    public List<College> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return repository.existsByCollegeCode(code);
    }

    private College toDomain(CollegeEntity entity) {
        List<Department> departments = entity.getDepartments().stream()
                .map(dept -> Department.builder()
                        .id(dept.getId())
                        .departmentCode(dept.getDepartmentCode())
                        .departmentName(dept.getDepartmentName())
                        .collegeId(entity.getId())
                        .collegeName(entity.getCollegeName())
                        .createdAt(dept.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return College.builder()
                .id(entity.getId())
                .collegeCode(entity.getCollegeCode())
                .collegeNumberCode(entity.getCollegeNumberCode())
                .collegeName(entity.getCollegeName())
                .departments(departments)
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private CollegeEntity toEntity(College domain) {
        return CollegeEntity.builder()
                .collegeCode(domain.getCollegeCode())
                .collegeNumberCode(domain.getCollegeNumberCode())
                .collegeName(domain.getCollegeName())
                .build();
    }
}

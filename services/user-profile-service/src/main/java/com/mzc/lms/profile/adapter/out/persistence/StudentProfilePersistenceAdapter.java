package com.mzc.lms.profile.adapter.out.persistence;

import com.mzc.lms.profile.adapter.out.persistence.entity.StudentProfileEntity;
import com.mzc.lms.profile.adapter.out.persistence.repository.StudentProfileJpaRepository;
import com.mzc.lms.profile.application.port.out.StudentProfileRepositoryPort;
import com.mzc.lms.profile.domain.model.StudentProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StudentProfilePersistenceAdapter implements StudentProfileRepositoryPort {

    private final StudentProfileJpaRepository repository;

    @Override
    public Optional<StudentProfile> findByUserId(Long userId) {
        return repository.findByUserId(userId)
                .map(this::toDomain);
    }

    @Override
    public Optional<StudentProfile> findByStudentId(Long studentId) {
        return repository.findByStudentId(studentId)
                .map(this::toDomain);
    }

    @Override
    public StudentProfile save(StudentProfile studentProfile) {
        StudentProfileEntity entity = toEntity(studentProfile);
        StudentProfileEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    private StudentProfile toDomain(StudentProfileEntity entity) {
        return StudentProfile.builder()
                .studentId(entity.getStudentId())
                .userId(entity.getUserId())
                .admissionYear(entity.getAdmissionYear())
                .grade(entity.getGrade())
                .departmentName(entity.getDepartmentName())
                .collegeName(entity.getCollegeName())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private StudentProfileEntity toEntity(StudentProfile domain) {
        return StudentProfileEntity.builder()
                .studentId(domain.getStudentId())
                .userId(domain.getUserId())
                .admissionYear(domain.getAdmissionYear())
                .grade(domain.getGrade())
                .departmentName(domain.getDepartmentName())
                .collegeName(domain.getCollegeName())
                .build();
    }
}

package com.mzc.lms.profile.adapter.out.persistence;

import com.mzc.lms.profile.adapter.out.persistence.entity.ProfessorProfileEntity;
import com.mzc.lms.profile.adapter.out.persistence.repository.ProfessorProfileJpaRepository;
import com.mzc.lms.profile.application.port.out.ProfessorProfileRepositoryPort;
import com.mzc.lms.profile.domain.model.ProfessorProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfessorProfilePersistenceAdapter implements ProfessorProfileRepositoryPort {

    private final ProfessorProfileJpaRepository repository;

    @Override
    public Optional<ProfessorProfile> findByUserId(Long userId) {
        return repository.findByUserId(userId)
                .map(this::toDomain);
    }

    @Override
    public Optional<ProfessorProfile> findByProfessorId(Long professorId) {
        return repository.findByProfessorId(professorId)
                .map(this::toDomain);
    }

    @Override
    public ProfessorProfile save(ProfessorProfile professorProfile) {
        ProfessorProfileEntity entity = toEntity(professorProfile);
        ProfessorProfileEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    private ProfessorProfile toDomain(ProfessorProfileEntity entity) {
        return ProfessorProfile.builder()
                .professorId(entity.getProfessorId())
                .userId(entity.getUserId())
                .appointmentDate(entity.getAppointmentDate())
                .departmentName(entity.getDepartmentName())
                .collegeName(entity.getCollegeName())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private ProfessorProfileEntity toEntity(ProfessorProfile domain) {
        return ProfessorProfileEntity.builder()
                .professorId(domain.getProfessorId())
                .userId(domain.getUserId())
                .appointmentDate(domain.getAppointmentDate())
                .departmentName(domain.getDepartmentName())
                .collegeName(domain.getCollegeName())
                .build();
    }
}

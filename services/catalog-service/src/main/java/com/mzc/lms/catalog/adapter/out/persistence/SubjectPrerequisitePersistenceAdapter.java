package com.mzc.lms.catalog.adapter.out.persistence;

import com.mzc.lms.catalog.adapter.out.persistence.entity.SubjectPrerequisiteEntity;
import com.mzc.lms.catalog.adapter.out.persistence.repository.SubjectPrerequisiteJpaRepository;
import com.mzc.lms.catalog.application.port.out.SubjectPrerequisiteRepositoryPort;
import com.mzc.lms.catalog.domain.model.SubjectPrerequisite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubjectPrerequisitePersistenceAdapter implements SubjectPrerequisiteRepositoryPort {

    private final SubjectPrerequisiteJpaRepository prerequisiteJpaRepository;

    @Override
    public SubjectPrerequisite save(SubjectPrerequisite prerequisite) {
        SubjectPrerequisiteEntity entity = SubjectPrerequisiteEntity.fromDomain(prerequisite);
        SubjectPrerequisiteEntity saved = prerequisiteJpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<SubjectPrerequisite> findById(Long id) {
        return prerequisiteJpaRepository.findById(id)
                .map(SubjectPrerequisiteEntity::toDomain);
    }

    @Override
    public List<SubjectPrerequisite> findBySubjectId(Long subjectId) {
        return prerequisiteJpaRepository.findBySubjectId(subjectId).stream()
                .map(SubjectPrerequisiteEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectPrerequisite> findByPrerequisiteId(Long prerequisiteId) {
        return prerequisiteJpaRepository.findByPrerequisiteId(prerequisiteId).stream()
                .map(SubjectPrerequisiteEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SubjectPrerequisite> findBySubjectIdAndPrerequisiteId(Long subjectId, Long prerequisiteId) {
        return prerequisiteJpaRepository.findBySubjectIdAndPrerequisiteId(subjectId, prerequisiteId)
                .map(SubjectPrerequisiteEntity::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        prerequisiteJpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBySubjectIdAndPrerequisiteId(Long subjectId, Long prerequisiteId) {
        prerequisiteJpaRepository.deleteBySubjectIdAndPrerequisiteId(subjectId, prerequisiteId);
    }

    @Override
    public boolean existsBySubjectIdAndPrerequisiteId(Long subjectId, Long prerequisiteId) {
        return prerequisiteJpaRepository.existsBySubjectIdAndPrerequisiteId(subjectId, prerequisiteId);
    }
}

package com.mzc.lms.assessment.adapter.out.persistence;

import com.mzc.lms.assessment.adapter.out.persistence.entity.AssessmentEntity;
import com.mzc.lms.assessment.adapter.out.persistence.repository.AssessmentJpaRepository;
import com.mzc.lms.assessment.application.port.out.AssessmentRepository;
import com.mzc.lms.assessment.domain.model.Assessment;
import com.mzc.lms.assessment.domain.model.AssessmentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AssessmentPersistenceAdapter implements AssessmentRepository {

    private final AssessmentJpaRepository assessmentJpaRepository;

    @Override
    public Assessment save(Assessment assessment) {
        AssessmentEntity entity = AssessmentEntity.fromDomain(assessment);
        AssessmentEntity savedEntity = assessmentJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Assessment> findById(Long id) {
        return assessmentJpaRepository.findById(id)
                .map(AssessmentEntity::toDomain);
    }

    @Override
    public List<Assessment> findByCourseId(Long courseId) {
        return assessmentJpaRepository.findByCourseId(courseId).stream()
                .map(AssessmentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Assessment> findByCourseIdAndType(Long courseId, AssessmentType type) {
        return assessmentJpaRepository.findByCourseIdAndType(courseId, type).stream()
                .map(AssessmentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Assessment> findByCourseIdAndIsPublishedTrue(Long courseId) {
        return assessmentJpaRepository.findByCourseIdAndIsPublishedTrue(courseId).stream()
                .map(AssessmentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        assessmentJpaRepository.deleteById(id);
    }
}

package com.mzc.lms.progress.adapter.out.persistence;

import com.mzc.lms.progress.adapter.out.persistence.entity.LearningProgressEntity;
import com.mzc.lms.progress.adapter.out.persistence.repository.LearningProgressJpaRepository;
import com.mzc.lms.progress.application.port.out.LearningProgressRepository;
import com.mzc.lms.progress.domain.model.LearningProgress;
import com.mzc.lms.progress.domain.model.ProgressStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LearningProgressPersistenceAdapter implements LearningProgressRepository {

    private final LearningProgressJpaRepository jpaRepository;

    @Override
    public LearningProgress save(LearningProgress progress) {
        LearningProgressEntity entity = LearningProgressEntity.fromDomain(progress);
        LearningProgressEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<LearningProgress> findById(Long id) {
        return jpaRepository.findById(id).map(LearningProgressEntity::toDomain);
    }

    @Override
    public Optional<LearningProgress> findByStudentIdAndCourseId(Long studentId, Long courseId) {
        return jpaRepository.findByStudentIdAndCourseId(studentId, courseId)
                .map(LearningProgressEntity::toDomain);
    }

    @Override
    public List<LearningProgress> findByStudentId(Long studentId) {
        return jpaRepository.findByStudentId(studentId)
                .stream()
                .map(LearningProgressEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<LearningProgress> findByCourseId(Long courseId) {
        return jpaRepository.findByCourseId(courseId)
                .stream()
                .map(LearningProgressEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<LearningProgress> findByStudentIdAndStatus(Long studentId, ProgressStatus status) {
        return jpaRepository.findByStudentIdAndStatus(studentId, status)
                .stream()
                .map(LearningProgressEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Double getAverageProgressByCourseId(Long courseId) {
        return jpaRepository.getAverageProgressByCourseId(courseId);
    }

    @Override
    public long countByStudentIdAndStatus(Long studentId, ProgressStatus status) {
        return jpaRepository.countByStudentIdAndStatus(studentId, status);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

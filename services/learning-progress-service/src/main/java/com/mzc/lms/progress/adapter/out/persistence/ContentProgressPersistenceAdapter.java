package com.mzc.lms.progress.adapter.out.persistence;

import com.mzc.lms.progress.adapter.out.persistence.entity.ContentProgressEntity;
import com.mzc.lms.progress.adapter.out.persistence.repository.ContentProgressJpaRepository;
import com.mzc.lms.progress.application.port.out.ContentProgressRepository;
import com.mzc.lms.progress.domain.model.ContentProgress;
import com.mzc.lms.progress.domain.model.ProgressStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ContentProgressPersistenceAdapter implements ContentProgressRepository {

    private final ContentProgressJpaRepository jpaRepository;

    @Override
    public ContentProgress save(ContentProgress progress) {
        ContentProgressEntity entity = ContentProgressEntity.fromDomain(progress);
        ContentProgressEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<ContentProgress> findById(Long id) {
        return jpaRepository.findById(id).map(ContentProgressEntity::toDomain);
    }

    @Override
    public Optional<ContentProgress> findByLearningProgressIdAndContentId(Long learningProgressId, Long contentId) {
        return jpaRepository.findByLearningProgressIdAndContentId(learningProgressId, contentId)
                .map(ContentProgressEntity::toDomain);
    }

    @Override
    public List<ContentProgress> findByLearningProgressId(Long learningProgressId) {
        return jpaRepository.findByLearningProgressId(learningProgressId)
                .stream()
                .map(ContentProgressEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countByLearningProgressIdAndStatus(Long learningProgressId, ProgressStatus status) {
        return jpaRepository.countByLearningProgressIdAndStatus(learningProgressId, status);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

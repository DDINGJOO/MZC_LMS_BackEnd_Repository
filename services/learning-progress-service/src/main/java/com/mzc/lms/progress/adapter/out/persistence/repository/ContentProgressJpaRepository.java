package com.mzc.lms.progress.adapter.out.persistence.repository;

import com.mzc.lms.progress.adapter.out.persistence.entity.ContentProgressEntity;
import com.mzc.lms.progress.domain.model.ProgressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentProgressJpaRepository extends JpaRepository<ContentProgressEntity, Long> {

    Optional<ContentProgressEntity> findByLearningProgressIdAndContentId(Long learningProgressId, Long contentId);

    List<ContentProgressEntity> findByLearningProgressId(Long learningProgressId);

    long countByLearningProgressIdAndStatus(Long learningProgressId, ProgressStatus status);
}

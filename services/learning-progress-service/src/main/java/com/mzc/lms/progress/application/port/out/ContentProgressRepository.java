package com.mzc.lms.progress.application.port.out;

import com.mzc.lms.progress.domain.model.ContentProgress;
import com.mzc.lms.progress.domain.model.ProgressStatus;

import java.util.List;
import java.util.Optional;

public interface ContentProgressRepository {

    ContentProgress save(ContentProgress progress);

    Optional<ContentProgress> findById(Long id);

    Optional<ContentProgress> findByLearningProgressIdAndContentId(Long learningProgressId, Long contentId);

    List<ContentProgress> findByLearningProgressId(Long learningProgressId);

    long countByLearningProgressIdAndStatus(Long learningProgressId, ProgressStatus status);

    void deleteById(Long id);
}

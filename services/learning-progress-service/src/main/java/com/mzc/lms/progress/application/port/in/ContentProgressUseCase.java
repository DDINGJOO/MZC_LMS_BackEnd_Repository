package com.mzc.lms.progress.application.port.in;

import com.mzc.lms.progress.domain.model.ContentProgress;

import java.util.List;
import java.util.Optional;

public interface ContentProgressUseCase {

    ContentProgress createContentProgress(Long learningProgressId, Long contentId, String contentType, Long totalTimeSeconds);

    ContentProgress startContent(Long contentProgressId);

    ContentProgress updateContentProgress(Long contentProgressId, Long watchedTimeSeconds);

    ContentProgress markContentComplete(Long contentProgressId);

    Optional<ContentProgress> findById(Long id);

    Optional<ContentProgress> findByLearningProgressIdAndContentId(Long learningProgressId, Long contentId);

    List<ContentProgress> findByLearningProgressId(Long learningProgressId);

    long countCompletedByLearningProgressId(Long learningProgressId);
}

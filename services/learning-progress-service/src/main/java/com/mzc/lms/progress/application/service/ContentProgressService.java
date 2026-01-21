package com.mzc.lms.progress.application.service;

import com.mzc.lms.progress.application.port.in.ContentProgressUseCase;
import com.mzc.lms.progress.application.port.out.ContentProgressRepository;
import com.mzc.lms.progress.application.port.out.LearningProgressRepository;
import com.mzc.lms.progress.application.port.out.ProgressEventPublisher;
import com.mzc.lms.progress.domain.event.LearningProgressEvent;
import com.mzc.lms.progress.domain.model.ContentProgress;
import com.mzc.lms.progress.domain.model.LearningProgress;
import com.mzc.lms.progress.domain.model.ProgressStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ContentProgressService implements ContentProgressUseCase {

    private final ContentProgressRepository contentProgressRepository;
    private final LearningProgressRepository learningProgressRepository;
    private final ProgressEventPublisher eventPublisher;

    @Override
    @CacheEvict(value = "content-progress-by-learning", key = "#learningProgressId")
    public ContentProgress createContentProgress(Long learningProgressId, Long contentId, String contentType, Long totalTimeSeconds) {
        log.info("Creating content progress for learning: {}, content: {}", learningProgressId, contentId);

        Optional<ContentProgress> existing = contentProgressRepository.findByLearningProgressIdAndContentId(learningProgressId, contentId);
        if (existing.isPresent()) {
            throw new IllegalStateException("Content progress already exists");
        }

        ContentProgress progress = ContentProgress.create(learningProgressId, contentId, contentType, totalTimeSeconds);
        ContentProgress saved = contentProgressRepository.save(progress);

        log.info("Content progress created with id: {}", saved.getId());
        return saved;
    }

    @Override
    @CacheEvict(value = "content-progress", key = "#contentProgressId")
    public ContentProgress startContent(Long contentProgressId) {
        log.info("Starting content progress: {}", contentProgressId);

        ContentProgress progress = contentProgressRepository.findById(contentProgressId)
                .orElseThrow(() -> new IllegalArgumentException("Content progress not found: " + contentProgressId));

        ContentProgress started = progress.start();
        return contentProgressRepository.save(started);
    }

    @Override
    @CacheEvict(value = "content-progress", key = "#contentProgressId")
    public ContentProgress updateContentProgress(Long contentProgressId, Long watchedTimeSeconds) {
        log.info("Updating content progress: {} with watched time: {}s", contentProgressId, watchedTimeSeconds);

        ContentProgress progress = contentProgressRepository.findById(contentProgressId)
                .orElseThrow(() -> new IllegalArgumentException("Content progress not found: " + contentProgressId));

        boolean wasCompleted = progress.isCompleted();
        ContentProgress updated = progress.updateProgress(watchedTimeSeconds);
        ContentProgress saved = contentProgressRepository.save(updated);

        if (saved.isCompleted() && !wasCompleted) {
            publishContentCompletedEvent(saved);
        }

        log.info("Content progress updated: {} to {}%", contentProgressId, saved.getProgressPercentage());
        return saved;
    }

    @Override
    @CacheEvict(value = "content-progress", key = "#contentProgressId")
    public ContentProgress markContentComplete(Long contentProgressId) {
        log.info("Marking content complete: {}", contentProgressId);

        ContentProgress progress = contentProgressRepository.findById(contentProgressId)
                .orElseThrow(() -> new IllegalArgumentException("Content progress not found: " + contentProgressId));

        if (progress.isCompleted()) {
            return progress;
        }

        ContentProgress completed = progress.markComplete();
        ContentProgress saved = contentProgressRepository.save(completed);

        publishContentCompletedEvent(saved);

        log.info("Content marked complete: {}", contentProgressId);
        return saved;
    }

    private void publishContentCompletedEvent(ContentProgress contentProgress) {
        LearningProgress learningProgress = learningProgressRepository.findById(contentProgress.getLearningProgressId())
                .orElse(null);

        if (learningProgress != null) {
            eventPublisher.publish(LearningProgressEvent.contentCompleted(
                    learningProgress.getId(),
                    learningProgress.getStudentId(),
                    learningProgress.getCourseId(),
                    contentProgress.getContentId()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "content-progress", key = "#id")
    public Optional<ContentProgress> findById(Long id) {
        return contentProgressRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContentProgress> findByLearningProgressIdAndContentId(Long learningProgressId, Long contentId) {
        return contentProgressRepository.findByLearningProgressIdAndContentId(learningProgressId, contentId);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "content-progress-by-learning", key = "#learningProgressId")
    public List<ContentProgress> findByLearningProgressId(Long learningProgressId) {
        return contentProgressRepository.findByLearningProgressId(learningProgressId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countCompletedByLearningProgressId(Long learningProgressId) {
        return contentProgressRepository.countByLearningProgressIdAndStatus(learningProgressId, ProgressStatus.COMPLETED);
    }
}

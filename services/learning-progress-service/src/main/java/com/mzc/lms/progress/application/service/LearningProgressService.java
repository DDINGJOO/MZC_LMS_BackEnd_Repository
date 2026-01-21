package com.mzc.lms.progress.application.service;

import com.mzc.lms.progress.application.port.in.LearningProgressUseCase;
import com.mzc.lms.progress.application.port.out.LearningProgressRepository;
import com.mzc.lms.progress.application.port.out.ProgressEventPublisher;
import com.mzc.lms.progress.domain.event.LearningProgressEvent;
import com.mzc.lms.progress.domain.model.LearningProgress;
import com.mzc.lms.progress.domain.model.ProgressStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LearningProgressService implements LearningProgressUseCase {

    private final LearningProgressRepository progressRepository;
    private final ProgressEventPublisher eventPublisher;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "progress-by-student", key = "#studentId"),
            @CacheEvict(value = "progress-by-course", key = "#courseId")
    })
    public LearningProgress createProgress(Long studentId, Long courseId, Long enrollmentId, Integer totalContents) {
        log.info("Creating learning progress for student: {}, course: {}", studentId, courseId);

        Optional<LearningProgress> existing = progressRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (existing.isPresent()) {
            throw new IllegalStateException("Progress already exists for this student and course");
        }

        LearningProgress progress = LearningProgress.create(studentId, courseId, enrollmentId, totalContents);
        LearningProgress saved = progressRepository.save(progress);

        log.info("Learning progress created with id: {}", saved.getId());
        return saved;
    }

    @Override
    @CacheEvict(value = "learning-progress", key = "#progressId")
    public LearningProgress startLearning(Long progressId) {
        log.info("Starting learning for progress: {}", progressId);

        LearningProgress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new IllegalArgumentException("Progress not found: " + progressId));

        LearningProgress started = progress.start();
        LearningProgress saved = progressRepository.save(started);

        eventPublisher.publish(LearningProgressEvent.started(saved.getId(), saved.getStudentId(), saved.getCourseId()));

        log.info("Learning started for progress: {}", progressId);
        return saved;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "learning-progress", key = "#progressId"),
            @CacheEvict(value = "progress-by-student", allEntries = true),
            @CacheEvict(value = "progress-by-course", allEntries = true)
    })
    public LearningProgress updateProgress(Long progressId, Integer completedContents, Long additionalTimeSeconds) {
        log.info("Updating progress: {} with completed: {}, time: {}s",
                progressId, completedContents, additionalTimeSeconds);

        LearningProgress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new IllegalArgumentException("Progress not found: " + progressId));

        ProgressStatus previousStatus = progress.getStatus();
        LearningProgress updated = progress.updateProgress(completedContents, additionalTimeSeconds);
        LearningProgress saved = progressRepository.save(updated);

        if (saved.getStatus() == ProgressStatus.COMPLETED && previousStatus != ProgressStatus.COMPLETED) {
            eventPublisher.publish(LearningProgressEvent.completed(saved.getId(), saved.getStudentId(), saved.getCourseId()));
        } else {
            eventPublisher.publish(LearningProgressEvent.progressUpdated(
                    saved.getId(), saved.getStudentId(), saved.getCourseId(), saved.getProgressPercentage()));
        }

        log.info("Progress updated: {} to {}%", progressId, saved.getProgressPercentage());
        return saved;
    }

    @Override
    @CacheEvict(value = "learning-progress", key = "#progressId")
    public LearningProgress addLearningTime(Long progressId, Long seconds) {
        log.info("Adding {} seconds to progress: {}", seconds, progressId);

        LearningProgress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new IllegalArgumentException("Progress not found: " + progressId));

        LearningProgress updated = progress.addLearningTime(seconds);
        return progressRepository.save(updated);
    }

    @Override
    @CacheEvict(value = "learning-progress", key = "#progressId")
    public LearningProgress pauseLearning(Long progressId) {
        log.info("Pausing learning for progress: {}", progressId);

        LearningProgress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new IllegalArgumentException("Progress not found: " + progressId));

        LearningProgress paused = progress.pause();
        return progressRepository.save(paused);
    }

    @Override
    @CacheEvict(value = "learning-progress", key = "#progressId")
    public LearningProgress resumeLearning(Long progressId) {
        log.info("Resuming learning for progress: {}", progressId);

        LearningProgress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new IllegalArgumentException("Progress not found: " + progressId));

        LearningProgress resumed = progress.resume();
        return progressRepository.save(resumed);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "learning-progress", key = "#id")
    public Optional<LearningProgress> findById(Long id) {
        return progressRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LearningProgress> findByStudentIdAndCourseId(Long studentId, Long courseId) {
        return progressRepository.findByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "progress-by-student", key = "#studentId")
    public List<LearningProgress> findByStudentId(Long studentId) {
        return progressRepository.findByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "progress-by-course", key = "#courseId")
    public List<LearningProgress> findByCourseId(Long courseId) {
        return progressRepository.findByCourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageProgressByCourseId(Long courseId) {
        return progressRepository.getAverageProgressByCourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countCompletedByStudentId(Long studentId) {
        return progressRepository.countByStudentIdAndStatus(studentId, ProgressStatus.COMPLETED);
    }
}

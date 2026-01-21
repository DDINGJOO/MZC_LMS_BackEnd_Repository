package com.mzc.lms.progress.application.port.in;

import com.mzc.lms.progress.domain.model.LearningProgress;

import java.util.List;
import java.util.Optional;

public interface LearningProgressUseCase {

    LearningProgress createProgress(Long studentId, Long courseId, Long enrollmentId, Integer totalContents);

    LearningProgress startLearning(Long progressId);

    LearningProgress updateProgress(Long progressId, Integer completedContents, Long additionalTimeSeconds);

    LearningProgress addLearningTime(Long progressId, Long seconds);

    LearningProgress pauseLearning(Long progressId);

    LearningProgress resumeLearning(Long progressId);

    Optional<LearningProgress> findById(Long id);

    Optional<LearningProgress> findByStudentIdAndCourseId(Long studentId, Long courseId);

    List<LearningProgress> findByStudentId(Long studentId);

    List<LearningProgress> findByCourseId(Long courseId);

    Double getAverageProgressByCourseId(Long courseId);

    long countCompletedByStudentId(Long studentId);
}

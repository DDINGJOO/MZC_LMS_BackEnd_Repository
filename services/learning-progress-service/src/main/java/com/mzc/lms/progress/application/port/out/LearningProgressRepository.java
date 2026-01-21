package com.mzc.lms.progress.application.port.out;

import com.mzc.lms.progress.domain.model.LearningProgress;
import com.mzc.lms.progress.domain.model.ProgressStatus;

import java.util.List;
import java.util.Optional;

public interface LearningProgressRepository {

    LearningProgress save(LearningProgress progress);

    Optional<LearningProgress> findById(Long id);

    Optional<LearningProgress> findByStudentIdAndCourseId(Long studentId, Long courseId);

    List<LearningProgress> findByStudentId(Long studentId);

    List<LearningProgress> findByCourseId(Long courseId);

    List<LearningProgress> findByStudentIdAndStatus(Long studentId, ProgressStatus status);

    Double getAverageProgressByCourseId(Long courseId);

    long countByStudentIdAndStatus(Long studentId, ProgressStatus status);

    void deleteById(Long id);
}

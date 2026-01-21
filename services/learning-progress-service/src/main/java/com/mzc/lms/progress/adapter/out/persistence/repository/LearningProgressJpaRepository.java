package com.mzc.lms.progress.adapter.out.persistence.repository;

import com.mzc.lms.progress.adapter.out.persistence.entity.LearningProgressEntity;
import com.mzc.lms.progress.domain.model.ProgressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearningProgressJpaRepository extends JpaRepository<LearningProgressEntity, Long> {

    Optional<LearningProgressEntity> findByStudentIdAndCourseId(Long studentId, Long courseId);

    List<LearningProgressEntity> findByStudentId(Long studentId);

    List<LearningProgressEntity> findByCourseId(Long courseId);

    List<LearningProgressEntity> findByStudentIdAndStatus(Long studentId, ProgressStatus status);

    @Query("SELECT AVG(p.progressPercentage) FROM LearningProgressEntity p WHERE p.courseId = :courseId")
    Double getAverageProgressByCourseId(@Param("courseId") Long courseId);

    long countByStudentIdAndStatus(Long studentId, ProgressStatus status);
}

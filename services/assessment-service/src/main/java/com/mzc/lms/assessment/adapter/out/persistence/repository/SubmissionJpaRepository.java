package com.mzc.lms.assessment.adapter.out.persistence.repository;

import com.mzc.lms.assessment.adapter.out.persistence.entity.SubmissionEntity;
import com.mzc.lms.assessment.domain.model.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionJpaRepository extends JpaRepository<SubmissionEntity, Long> {

    List<SubmissionEntity> findByAssessmentIdAndStudentIdOrderByAttemptNumberDesc(Long assessmentId, Long studentId);

    List<SubmissionEntity> findByAssessmentId(Long assessmentId);

    List<SubmissionEntity> findByStudentId(Long studentId);

    Optional<SubmissionEntity> findByAssessmentIdAndStudentIdAndStatus(Long assessmentId, Long studentId, SubmissionStatus status);

    @Query("SELECT COUNT(s) FROM SubmissionEntity s WHERE s.assessmentId = :assessmentId AND s.studentId = :studentId")
    int countByAssessmentIdAndStudentId(@Param("assessmentId") Long assessmentId, @Param("studentId") Long studentId);

    @Query("SELECT s FROM SubmissionEntity s WHERE s.assessmentId = :assessmentId AND s.studentId = :studentId AND s.isPassed = true")
    Optional<SubmissionEntity> findPassedSubmission(@Param("assessmentId") Long assessmentId, @Param("studentId") Long studentId);
}

package com.mzc.lms.assessment.application.port.out;

import com.mzc.lms.assessment.domain.model.Submission;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository {

    Submission save(Submission submission);

    Optional<Submission> findById(Long id);

    List<Submission> findByAssessmentId(Long assessmentId);

    List<Submission> findByStudentId(Long studentId);

    List<Submission> findByAssessmentIdAndStudentId(Long assessmentId, Long studentId);

    Integer countByAssessmentIdAndStudentId(Long assessmentId, Long studentId);

    void deleteById(Long id);
}

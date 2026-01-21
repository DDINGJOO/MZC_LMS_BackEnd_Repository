package com.mzc.lms.assessment.application.port.in;

import com.mzc.lms.assessment.domain.model.Submission;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SubmissionUseCase {

    Submission createSubmission(Long assessmentId, Long studentId);

    Submission startSubmission(Long id);

    Submission saveAnswers(Long id, Map<Long, String> answers);

    Submission submitSubmission(Long id);

    Submission gradeSubmission(Long id, Integer earnedPoints, String feedback, String gradedBy);

    Submission autoGradeSubmission(Long id);

    Optional<Submission> findById(Long id);

    List<Submission> findByAssessmentId(Long assessmentId);

    List<Submission> findByStudentId(Long studentId);

    Optional<Submission> findByAssessmentIdAndStudentId(Long assessmentId, Long studentId);

    Integer getAttemptCount(Long assessmentId, Long studentId);
}

package com.mzc.lms.assessment.application.service;

import com.mzc.lms.assessment.application.port.in.SubmissionUseCase;
import com.mzc.lms.assessment.application.port.out.AssessmentEventPublisher;
import com.mzc.lms.assessment.application.port.out.AssessmentRepository;
import com.mzc.lms.assessment.application.port.out.QuestionRepository;
import com.mzc.lms.assessment.application.port.out.SubmissionRepository;
import com.mzc.lms.assessment.domain.event.AssessmentEvent;
import com.mzc.lms.assessment.domain.model.Assessment;
import com.mzc.lms.assessment.domain.model.Question;
import com.mzc.lms.assessment.domain.model.Submission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionService implements SubmissionUseCase {

    private final SubmissionRepository submissionRepository;
    private final AssessmentRepository assessmentRepository;
    private final QuestionRepository questionRepository;
    private final AssessmentEventPublisher eventPublisher;

    @Override
    @CacheEvict(value = "submissions-by-assessment", key = "#assessmentId")
    public Submission createSubmission(Long assessmentId, Long studentId) {
        log.info("Creating submission for assessment: {}, student: {}", assessmentId, studentId);

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found: " + assessmentId));

        Integer attemptCount = submissionRepository.countByAssessmentIdAndStudentId(assessmentId, studentId);
        if (assessment.getMaxAttempts() != null && attemptCount >= assessment.getMaxAttempts()) {
            throw new IllegalStateException("Maximum attempts reached for this assessment");
        }

        Submission submission = Submission.create(assessmentId, studentId, attemptCount + 1, assessment.getTotalPoints());
        Submission saved = submissionRepository.save(submission);

        log.info("Submission created with id: {}", saved.getId());
        return saved;
    }

    @Override
    @CacheEvict(value = "submission", key = "#id")
    public Submission startSubmission(Long id) {
        log.info("Starting submission: {}", id);

        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found: " + id));

        Submission started = submission.start();
        return submissionRepository.save(started);
    }

    @Override
    @CacheEvict(value = "submission", key = "#id")
    public Submission saveAnswers(Long id, Map<Long, String> answers) {
        log.info("Saving answers for submission: {}", id);

        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found: " + id));

        Submission updated = submission.saveAnswers(answers);
        return submissionRepository.save(updated);
    }

    @Override
    @CacheEvict(value = "submission", key = "#id")
    public Submission submitSubmission(Long id) {
        log.info("Submitting submission: {}", id);

        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found: " + id));

        Submission submitted = submission.submit();
        Submission saved = submissionRepository.save(submitted);

        eventPublisher.publish(AssessmentEvent.submitted(saved.getAssessmentId(), saved.getStudentId(), saved.getId()));

        log.info("Submission submitted: {}", id);
        return saved;
    }

    @Override
    @CacheEvict(value = "submission", key = "#id")
    public Submission gradeSubmission(Long id, Integer earnedPoints, String feedback, String gradedBy) {
        log.info("Grading submission: {}", id);

        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found: " + id));

        Assessment assessment = assessmentRepository.findById(submission.getAssessmentId())
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found"));

        Submission graded = submission.grade(earnedPoints, assessment.getPassingPoints(), feedback, gradedBy);
        Submission saved = submissionRepository.save(graded);

        eventPublisher.publish(AssessmentEvent.graded(
                saved.getAssessmentId(), saved.getStudentId(), saved.getId(),
                saved.getEarnedPoints(), saved.getIsPassed()));

        log.info("Submission graded: {}, score: {}", id, earnedPoints);
        return saved;
    }

    @Override
    @CacheEvict(value = "submission", key = "#id")
    public Submission autoGradeSubmission(Long id) {
        log.info("Auto-grading submission: {}", id);

        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found: " + id));

        List<Question> questions = questionRepository.findByAssessmentId(submission.getAssessmentId());
        Map<Long, String> answers = submission.getAnswers();

        int totalEarned = 0;
        for (Question question : questions) {
            if (question.getType().isAutoGradable() && answers != null) {
                String answer = answers.get(question.getId());
                Integer earned = question.getEarnedPoints(answer);
                if (earned != null) {
                    totalEarned += earned;
                }
            }
        }

        return gradeSubmission(id, totalEarned, "Auto-graded", "SYSTEM");
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "submission", key = "#id")
    public Optional<Submission> findById(Long id) {
        return submissionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "submissions-by-assessment", key = "#assessmentId")
    public List<Submission> findByAssessmentId(Long assessmentId) {
        return submissionRepository.findByAssessmentId(assessmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Submission> findByStudentId(Long studentId) {
        return submissionRepository.findByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Submission> findByAssessmentIdAndStudentId(Long assessmentId, Long studentId) {
        List<Submission> submissions = submissionRepository.findByAssessmentIdAndStudentId(assessmentId, studentId);
        return submissions.isEmpty() ? Optional.empty() : Optional.of(submissions.get(submissions.size() - 1));
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getAttemptCount(Long assessmentId, Long studentId) {
        return submissionRepository.countByAssessmentIdAndStudentId(assessmentId, studentId);
    }
}

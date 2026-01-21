package com.mzc.lms.assessment.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class Submission {
    private Long id;
    private Long assessmentId;
    private Long studentId;
    private Integer attemptNumber;
    private SubmissionStatus status;
    private Map<Long, String> answers;
    private Integer earnedPoints;
    private Integer totalPoints;
    private Double scorePercentage;
    private Boolean isPassed;
    private String feedback;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private LocalDateTime gradedAt;
    private String gradedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Submission create(Long assessmentId, Long studentId, Integer attemptNumber, Integer totalPoints) {
        return Submission.builder()
                .assessmentId(assessmentId)
                .studentId(studentId)
                .attemptNumber(attemptNumber)
                .status(SubmissionStatus.NOT_STARTED)
                .totalPoints(totalPoints)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Submission start() {
        if (this.status != SubmissionStatus.NOT_STARTED) {
            throw new IllegalStateException("Submission has already started");
        }
        return Submission.builder()
                .id(this.id)
                .assessmentId(this.assessmentId)
                .studentId(this.studentId)
                .attemptNumber(this.attemptNumber)
                .status(SubmissionStatus.IN_PROGRESS)
                .answers(this.answers)
                .earnedPoints(this.earnedPoints)
                .totalPoints(this.totalPoints)
                .scorePercentage(this.scorePercentage)
                .isPassed(this.isPassed)
                .feedback(this.feedback)
                .startedAt(LocalDateTime.now())
                .submittedAt(this.submittedAt)
                .gradedAt(this.gradedAt)
                .gradedBy(this.gradedBy)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Submission saveAnswers(Map<Long, String> answers) {
        if (!this.status.canSubmit()) {
            throw new IllegalStateException("Cannot save answers in status: " + this.status);
        }
        return Submission.builder()
                .id(this.id)
                .assessmentId(this.assessmentId)
                .studentId(this.studentId)
                .attemptNumber(this.attemptNumber)
                .status(SubmissionStatus.IN_PROGRESS)
                .answers(answers)
                .earnedPoints(this.earnedPoints)
                .totalPoints(this.totalPoints)
                .scorePercentage(this.scorePercentage)
                .isPassed(this.isPassed)
                .feedback(this.feedback)
                .startedAt(this.startedAt)
                .submittedAt(this.submittedAt)
                .gradedAt(this.gradedAt)
                .gradedBy(this.gradedBy)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Submission submit() {
        if (!this.status.canSubmit()) {
            throw new IllegalStateException("Cannot submit in status: " + this.status);
        }
        return Submission.builder()
                .id(this.id)
                .assessmentId(this.assessmentId)
                .studentId(this.studentId)
                .attemptNumber(this.attemptNumber)
                .status(SubmissionStatus.SUBMITTED)
                .answers(this.answers)
                .earnedPoints(this.earnedPoints)
                .totalPoints(this.totalPoints)
                .scorePercentage(this.scorePercentage)
                .isPassed(this.isPassed)
                .feedback(this.feedback)
                .startedAt(this.startedAt)
                .submittedAt(LocalDateTime.now())
                .gradedAt(this.gradedAt)
                .gradedBy(this.gradedBy)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Submission grade(Integer earnedPoints, Integer passingPoints, String feedback, String gradedBy) {
        if (!this.status.canGrade()) {
            throw new IllegalStateException("Cannot grade in status: " + this.status);
        }
        double percentage = this.totalPoints > 0 ? (double) earnedPoints / this.totalPoints * 100 : 0;
        boolean passed = passingPoints != null && earnedPoints >= passingPoints;

        return Submission.builder()
                .id(this.id)
                .assessmentId(this.assessmentId)
                .studentId(this.studentId)
                .attemptNumber(this.attemptNumber)
                .status(SubmissionStatus.GRADED)
                .answers(this.answers)
                .earnedPoints(earnedPoints)
                .totalPoints(this.totalPoints)
                .scorePercentage(percentage)
                .isPassed(passed)
                .feedback(feedback)
                .startedAt(this.startedAt)
                .submittedAt(this.submittedAt)
                .gradedAt(LocalDateTime.now())
                .gradedBy(gradedBy)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}

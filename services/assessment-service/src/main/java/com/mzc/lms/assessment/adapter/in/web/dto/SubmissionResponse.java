package com.mzc.lms.assessment.adapter.in.web.dto;

import com.mzc.lms.assessment.domain.model.Submission;
import com.mzc.lms.assessment.domain.model.SubmissionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class SubmissionResponse {

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

    public static SubmissionResponse from(Submission submission) {
        return SubmissionResponse.builder()
                .id(submission.getId())
                .assessmentId(submission.getAssessmentId())
                .studentId(submission.getStudentId())
                .attemptNumber(submission.getAttemptNumber())
                .status(submission.getStatus())
                .answers(submission.getAnswers())
                .earnedPoints(submission.getEarnedPoints())
                .totalPoints(submission.getTotalPoints())
                .scorePercentage(submission.getScorePercentage())
                .isPassed(submission.getIsPassed())
                .feedback(submission.getFeedback())
                .startedAt(submission.getStartedAt())
                .submittedAt(submission.getSubmittedAt())
                .gradedAt(submission.getGradedAt())
                .gradedBy(submission.getGradedBy())
                .createdAt(submission.getCreatedAt())
                .updatedAt(submission.getUpdatedAt())
                .build();
    }
}

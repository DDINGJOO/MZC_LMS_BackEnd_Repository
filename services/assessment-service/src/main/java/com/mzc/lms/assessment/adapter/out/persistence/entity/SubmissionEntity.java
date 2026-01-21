package com.mzc.lms.assessment.adapter.out.persistence.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mzc.lms.assessment.domain.model.Submission;
import com.mzc.lms.assessment.domain.model.SubmissionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "submissions",
        indexes = {
                @Index(name = "idx_submission_assessment", columnList = "assessment_id"),
                @Index(name = "idx_submission_student", columnList = "student_id"),
                @Index(name = "idx_submission_status", columnList = "status")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionEntity {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_id", nullable = false)
    private Long assessmentId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubmissionStatus status;

    @Column(columnDefinition = "TEXT")
    private String answers;

    @Column(name = "earned_points")
    private Integer earnedPoints;

    @Column(name = "total_points")
    private Integer totalPoints;

    @Column(name = "score_percentage")
    private Double scorePercentage;

    @Column(name = "is_passed")
    private Boolean isPassed;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "graded_at")
    private LocalDateTime gradedAt;

    @Column(name = "graded_by", length = 100)
    private String gradedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static SubmissionEntity fromDomain(Submission submission) {
        String answersJson = null;
        if (submission.getAnswers() != null) {
            try {
                answersJson = objectMapper.writeValueAsString(submission.getAnswers());
            } catch (JsonProcessingException e) {
                answersJson = "{}";
            }
        }

        return SubmissionEntity.builder()
                .id(submission.getId())
                .assessmentId(submission.getAssessmentId())
                .studentId(submission.getStudentId())
                .attemptNumber(submission.getAttemptNumber())
                .status(submission.getStatus())
                .answers(answersJson)
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

    public Submission toDomain() {
        Map<Long, String> answersMap = new HashMap<>();
        if (this.answers != null) {
            try {
                answersMap = objectMapper.readValue(this.answers, new TypeReference<Map<Long, String>>() {});
            } catch (JsonProcessingException e) {
                answersMap = new HashMap<>();
            }
        }

        return Submission.builder()
                .id(this.id)
                .assessmentId(this.assessmentId)
                .studentId(this.studentId)
                .attemptNumber(this.attemptNumber)
                .status(this.status)
                .answers(answersMap)
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
                .updatedAt(this.updatedAt)
                .build();
    }
}

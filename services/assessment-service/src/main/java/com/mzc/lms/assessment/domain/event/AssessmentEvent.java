package com.mzc.lms.assessment.domain.event;

import com.mzc.lms.assessment.domain.model.AssessmentType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AssessmentEvent {
    private String eventId;
    private String eventType;
    private Long assessmentId;
    private Long courseId;
    private Long studentId;
    private Long submissionId;
    private AssessmentType assessmentType;
    private Integer score;
    private Boolean passed;
    private LocalDateTime occurredAt;

    public static AssessmentEvent published(Long assessmentId, Long courseId, AssessmentType type) {
        return AssessmentEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("ASSESSMENT_PUBLISHED")
                .assessmentId(assessmentId)
                .courseId(courseId)
                .assessmentType(type)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static AssessmentEvent submitted(Long assessmentId, Long studentId, Long submissionId) {
        return AssessmentEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("SUBMISSION_SUBMITTED")
                .assessmentId(assessmentId)
                .studentId(studentId)
                .submissionId(submissionId)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static AssessmentEvent graded(Long assessmentId, Long studentId, Long submissionId,
                                          Integer score, Boolean passed) {
        return AssessmentEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("SUBMISSION_GRADED")
                .assessmentId(assessmentId)
                .studentId(studentId)
                .submissionId(submissionId)
                .score(score)
                .passed(passed)
                .occurredAt(LocalDateTime.now())
                .build();
    }
}

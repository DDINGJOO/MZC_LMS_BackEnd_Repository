package com.mzc.lms.progress.domain.event;

import com.mzc.lms.progress.domain.model.ProgressStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LearningProgressEvent {
    private String eventId;
    private String eventType;
    private Long progressId;
    private Long studentId;
    private Long courseId;
    private Long contentId;
    private ProgressStatus status;
    private Double progressPercentage;
    private LocalDateTime occurredAt;

    public static LearningProgressEvent started(Long progressId, Long studentId, Long courseId) {
        return LearningProgressEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("LEARNING_STARTED")
                .progressId(progressId)
                .studentId(studentId)
                .courseId(courseId)
                .status(ProgressStatus.IN_PROGRESS)
                .progressPercentage(0.0)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static LearningProgressEvent progressUpdated(Long progressId, Long studentId, Long courseId, Double percentage) {
        return LearningProgressEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("PROGRESS_UPDATED")
                .progressId(progressId)
                .studentId(studentId)
                .courseId(courseId)
                .status(ProgressStatus.IN_PROGRESS)
                .progressPercentage(percentage)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static LearningProgressEvent completed(Long progressId, Long studentId, Long courseId) {
        return LearningProgressEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("LEARNING_COMPLETED")
                .progressId(progressId)
                .studentId(studentId)
                .courseId(courseId)
                .status(ProgressStatus.COMPLETED)
                .progressPercentage(100.0)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static LearningProgressEvent contentCompleted(Long progressId, Long studentId, Long courseId, Long contentId) {
        return LearningProgressEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("CONTENT_COMPLETED")
                .progressId(progressId)
                .studentId(studentId)
                .courseId(courseId)
                .contentId(contentId)
                .status(ProgressStatus.COMPLETED)
                .progressPercentage(100.0)
                .occurredAt(LocalDateTime.now())
                .build();
    }
}

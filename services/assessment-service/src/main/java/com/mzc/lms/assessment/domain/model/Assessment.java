package com.mzc.lms.assessment.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Assessment {
    private Long id;
    private Long courseId;
    private String title;
    private String description;
    private AssessmentType type;
    private Integer totalPoints;
    private Integer passingPoints;
    private Integer timeLimitMinutes;
    private Integer maxAttempts;
    private Boolean shuffleQuestions;
    private Boolean showCorrectAnswers;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isPublished;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Assessment create(Long courseId, String title, String description,
                                    AssessmentType type, Integer totalPoints, Integer passingPoints) {
        return Assessment.builder()
                .courseId(courseId)
                .title(title)
                .description(description)
                .type(type)
                .totalPoints(totalPoints)
                .passingPoints(passingPoints)
                .timeLimitMinutes(null)
                .maxAttempts(1)
                .shuffleQuestions(false)
                .showCorrectAnswers(false)
                .isPublished(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Assessment update(String title, String description, Integer totalPoints,
                             Integer passingPoints, Integer timeLimitMinutes) {
        return Assessment.builder()
                .id(this.id)
                .courseId(this.courseId)
                .title(title)
                .description(description)
                .type(this.type)
                .totalPoints(totalPoints)
                .passingPoints(passingPoints)
                .timeLimitMinutes(timeLimitMinutes)
                .maxAttempts(this.maxAttempts)
                .shuffleQuestions(this.shuffleQuestions)
                .showCorrectAnswers(this.showCorrectAnswers)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .isPublished(this.isPublished)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Assessment publish() {
        if (this.isPublished) {
            throw new IllegalStateException("Assessment is already published");
        }
        return Assessment.builder()
                .id(this.id)
                .courseId(this.courseId)
                .title(this.title)
                .description(this.description)
                .type(this.type)
                .totalPoints(this.totalPoints)
                .passingPoints(this.passingPoints)
                .timeLimitMinutes(this.timeLimitMinutes)
                .maxAttempts(this.maxAttempts)
                .shuffleQuestions(this.shuffleQuestions)
                .showCorrectAnswers(this.showCorrectAnswers)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .isPublished(true)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Assessment setSchedule(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        return Assessment.builder()
                .id(this.id)
                .courseId(this.courseId)
                .title(this.title)
                .description(this.description)
                .type(this.type)
                .totalPoints(this.totalPoints)
                .passingPoints(this.passingPoints)
                .timeLimitMinutes(this.timeLimitMinutes)
                .maxAttempts(this.maxAttempts)
                .shuffleQuestions(this.shuffleQuestions)
                .showCorrectAnswers(this.showCorrectAnswers)
                .startDate(startDate)
                .endDate(endDate)
                .isPublished(this.isPublished)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public boolean isAvailable() {
        if (!this.isPublished) return false;
        LocalDateTime now = LocalDateTime.now();
        if (this.startDate != null && now.isBefore(this.startDate)) return false;
        if (this.endDate != null && now.isAfter(this.endDate)) return false;
        return true;
    }
}

package com.mzc.lms.assessment.adapter.in.web.dto;

import com.mzc.lms.assessment.domain.model.Assessment;
import com.mzc.lms.assessment.domain.model.AssessmentType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AssessmentResponse {

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

    public static AssessmentResponse from(Assessment assessment) {
        return AssessmentResponse.builder()
                .id(assessment.getId())
                .courseId(assessment.getCourseId())
                .title(assessment.getTitle())
                .description(assessment.getDescription())
                .type(assessment.getType())
                .totalPoints(assessment.getTotalPoints())
                .passingPoints(assessment.getPassingPoints())
                .timeLimitMinutes(assessment.getTimeLimitMinutes())
                .maxAttempts(assessment.getMaxAttempts())
                .shuffleQuestions(assessment.getShuffleQuestions())
                .showCorrectAnswers(assessment.getShowCorrectAnswers())
                .startDate(assessment.getStartDate())
                .endDate(assessment.getEndDate())
                .isPublished(assessment.getIsPublished())
                .createdAt(assessment.getCreatedAt())
                .updatedAt(assessment.getUpdatedAt())
                .build();
    }
}

package com.mzc.lms.assessment.adapter.in.web.dto;

import com.mzc.lms.assessment.domain.model.AssessmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateAssessmentRequest {

    @NotBlank(message = "제목은 필수입니다")
    private String title;

    private String description;

    private AssessmentType type;

    @Positive(message = "총점은 양수여야 합니다")
    private Integer totalPoints;

    private Integer passingPoints;

    private Integer timeLimitMinutes;

    private Integer maxAttempts;

    private Boolean shuffleQuestions;

    private Boolean showCorrectAnswers;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}

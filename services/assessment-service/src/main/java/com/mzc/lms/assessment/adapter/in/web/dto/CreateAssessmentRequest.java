package com.mzc.lms.assessment.adapter.in.web.dto;

import com.mzc.lms.assessment.domain.model.AssessmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateAssessmentRequest {

    @NotNull(message = "코스 ID는 필수입니다")
    private Long courseId;

    @NotBlank(message = "제목은 필수입니다")
    private String title;

    private String description;

    @NotNull(message = "평가 유형은 필수입니다")
    private AssessmentType type;

    @NotNull(message = "총점은 필수입니다")
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

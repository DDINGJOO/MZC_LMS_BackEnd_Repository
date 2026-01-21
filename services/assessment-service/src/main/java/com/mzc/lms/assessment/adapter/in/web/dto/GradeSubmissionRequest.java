package com.mzc.lms.assessment.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeSubmissionRequest {

    @NotNull(message = "획득 점수는 필수입니다")
    @PositiveOrZero(message = "획득 점수는 0 이상이어야 합니다")
    private Integer earnedPoints;

    @NotNull(message = "총점은 필수입니다")
    @PositiveOrZero(message = "총점은 0 이상이어야 합니다")
    private Integer totalPoints;

    private Integer passingPoints;

    private String feedback;

    @NotBlank(message = "채점자 정보는 필수입니다")
    private String gradedBy;
}

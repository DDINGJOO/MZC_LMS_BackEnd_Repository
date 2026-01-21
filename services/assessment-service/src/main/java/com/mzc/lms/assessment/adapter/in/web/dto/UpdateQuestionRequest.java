package com.mzc.lms.assessment.adapter.in.web.dto;

import com.mzc.lms.assessment.domain.model.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateQuestionRequest {

    private QuestionType type;

    @NotBlank(message = "문제 내용은 필수입니다")
    private String questionText;

    private List<String> options;

    private String correctAnswer;

    @Positive(message = "배점은 양수여야 합니다")
    private Integer points;

    private Integer orderIndex;

    private String explanation;
}

package com.mzc.lms.assessment.adapter.in.web.dto;

import com.mzc.lms.assessment.domain.model.Question;
import com.mzc.lms.assessment.domain.model.QuestionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class QuestionResponse {

    private Long id;
    private Long assessmentId;
    private QuestionType type;
    private String questionText;
    private List<String> options;
    private String correctAnswer;
    private Integer points;
    private Integer orderIndex;
    private String explanation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static QuestionResponse from(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .assessmentId(question.getAssessmentId())
                .type(question.getType())
                .questionText(question.getQuestionText())
                .options(question.getOptions())
                .correctAnswer(question.getCorrectAnswer())
                .points(question.getPoints())
                .orderIndex(question.getOrderIndex())
                .explanation(question.getExplanation())
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }

    public static QuestionResponse fromWithoutAnswer(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .assessmentId(question.getAssessmentId())
                .type(question.getType())
                .questionText(question.getQuestionText())
                .options(question.getOptions())
                .points(question.getPoints())
                .orderIndex(question.getOrderIndex())
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }
}

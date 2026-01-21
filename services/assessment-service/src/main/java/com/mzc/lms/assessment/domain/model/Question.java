package com.mzc.lms.assessment.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class Question {
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

    public static Question create(Long assessmentId, QuestionType type, String questionText,
                                   List<String> options, String correctAnswer, Integer points, Integer orderIndex) {
        return Question.builder()
                .assessmentId(assessmentId)
                .type(type)
                .questionText(questionText)
                .options(options)
                .correctAnswer(correctAnswer)
                .points(points)
                .orderIndex(orderIndex)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Question update(String questionText, List<String> options, String correctAnswer,
                           Integer points, String explanation) {
        return Question.builder()
                .id(this.id)
                .assessmentId(this.assessmentId)
                .type(this.type)
                .questionText(questionText)
                .options(options)
                .correctAnswer(correctAnswer)
                .points(points)
                .orderIndex(this.orderIndex)
                .explanation(explanation)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public boolean isCorrect(String answer) {
        if (this.correctAnswer == null || answer == null) {
            return false;
        }
        return this.correctAnswer.equalsIgnoreCase(answer.trim());
    }

    public Integer getEarnedPoints(String answer) {
        if (this.type.isAutoGradable()) {
            return isCorrect(answer) ? this.points : 0;
        }
        return null;
    }
}

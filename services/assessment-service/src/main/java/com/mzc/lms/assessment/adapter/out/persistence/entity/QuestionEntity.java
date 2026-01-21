package com.mzc.lms.assessment.adapter.out.persistence.entity;

import com.mzc.lms.assessment.domain.model.Question;
import com.mzc.lms.assessment.domain.model.QuestionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "questions",
        indexes = {
                @Index(name = "idx_question_assessment", columnList = "assessment_id"),
                @Index(name = "idx_question_order", columnList = "order_index")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_id", nullable = false)
    private Long assessmentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private QuestionType type;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(columnDefinition = "TEXT")
    private String options;

    @Column(name = "correct_answer", columnDefinition = "TEXT")
    private String correctAnswer;

    @Column(nullable = false)
    private Integer points;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static QuestionEntity fromDomain(Question question) {
        String optionsStr = question.getOptions() != null ? String.join("|||", question.getOptions()) : null;
        return QuestionEntity.builder()
                .id(question.getId())
                .assessmentId(question.getAssessmentId())
                .type(question.getType())
                .questionText(question.getQuestionText())
                .options(optionsStr)
                .correctAnswer(question.getCorrectAnswer())
                .points(question.getPoints())
                .orderIndex(question.getOrderIndex())
                .explanation(question.getExplanation())
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }

    public Question toDomain() {
        List<String> optionsList = this.options != null ? Arrays.asList(this.options.split("\\|\\|\\|")) : null;
        return Question.builder()
                .id(this.id)
                .assessmentId(this.assessmentId)
                .type(this.type)
                .questionText(this.questionText)
                .options(optionsList)
                .correctAnswer(this.correctAnswer)
                .points(this.points)
                .orderIndex(this.orderIndex)
                .explanation(this.explanation)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}

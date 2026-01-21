package com.mzc.lms.assessment.adapter.out.persistence.entity;

import com.mzc.lms.assessment.domain.model.Assessment;
import com.mzc.lms.assessment.domain.model.AssessmentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "assessments",
        indexes = {
                @Index(name = "idx_assessment_course", columnList = "course_id"),
                @Index(name = "idx_assessment_type", columnList = "type"),
                @Index(name = "idx_assessment_published", columnList = "is_published")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AssessmentType type;

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints;

    @Column(name = "passing_points")
    private Integer passingPoints;

    @Column(name = "time_limit_minutes")
    private Integer timeLimitMinutes;

    @Column(name = "max_attempts")
    private Integer maxAttempts;

    @Column(name = "shuffle_questions")
    private Boolean shuffleQuestions;

    @Column(name = "show_correct_answers")
    private Boolean showCorrectAnswers;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "is_published")
    private Boolean isPublished;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static AssessmentEntity fromDomain(Assessment assessment) {
        return AssessmentEntity.builder()
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

    public Assessment toDomain() {
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
                .isPublished(this.isPublished)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}

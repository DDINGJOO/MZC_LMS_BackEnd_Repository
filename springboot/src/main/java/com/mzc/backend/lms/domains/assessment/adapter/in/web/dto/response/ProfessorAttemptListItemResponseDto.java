package com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.response;

import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.AssessmentAttempt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ProfessorAttemptListItemResponseDto {

    private Long attemptId;
    private Long examId;
    private Long courseId;

    private StudentInfo student;

    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;

    private Boolean isLate;
    private BigDecimal latePenaltyRate;

    private BigDecimal score;
    private String feedback;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class StudentInfo {
        private Long id;
        private String studentNumber;
        private String name;
    }

    public static ProfessorAttemptListItemResponseDto from(
            AssessmentAttempt attempt,
            StudentInfo studentInfo
    ) {
        return ProfessorAttemptListItemResponseDto.builder()
                .attemptId(attempt.getId())
                .examId(attempt.getAssessment().getId())
                .courseId(attempt.getAssessment().getCourseId())
                .student(studentInfo)
                .startedAt(attempt.getStartedAt())
                .submittedAt(attempt.getSubmittedAt())
                .isLate(attempt.getIsLate())
                .latePenaltyRate(attempt.getLatePenaltyRate())
                .score(attempt.getScore())
                .feedback(attempt.getFeedback())
                .build();
    }
}



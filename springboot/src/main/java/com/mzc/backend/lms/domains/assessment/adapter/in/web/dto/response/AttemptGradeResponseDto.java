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
public class AttemptGradeResponseDto {

    private Long attemptId;
    private BigDecimal score;
    private Boolean isLate;
    private BigDecimal latePenaltyRate;
    private LocalDateTime gradedAt;
    private Long gradedBy;

    public static AttemptGradeResponseDto from(AssessmentAttempt attempt) {
        return AttemptGradeResponseDto.builder()
                .attemptId(attempt.getId())
                .score(attempt.getScore())
                .isLate(attempt.getIsLate())
                .latePenaltyRate(attempt.getLatePenaltyRate())
                .gradedAt(attempt.getGradedAt())
                .gradedBy(attempt.getGradedBy())
                .build();
    }
}



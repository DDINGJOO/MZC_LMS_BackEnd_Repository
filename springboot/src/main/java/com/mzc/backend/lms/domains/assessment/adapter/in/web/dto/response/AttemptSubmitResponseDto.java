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
public class AttemptSubmitResponseDto {

    private Long attemptId;
    private LocalDateTime submittedAt;
    private Boolean isLate;
    private BigDecimal latePenaltyRate;
    private BigDecimal score;

    public static AttemptSubmitResponseDto from(AssessmentAttempt attempt) {
        return AttemptSubmitResponseDto.builder()
                .attemptId(attempt.getId())
                .submittedAt(attempt.getSubmittedAt())
                .isLate(attempt.getIsLate())
                .latePenaltyRate(attempt.getLatePenaltyRate())
                .score(attempt.getScore())
                .build();
    }
}



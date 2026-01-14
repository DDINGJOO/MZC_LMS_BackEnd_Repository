package com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.response;

import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.AssessmentAttempt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Builder
@AllArgsConstructor
public class AttemptStartResponseDto {

    private Long attemptId;
    private LocalDateTime startedAt;
    private LocalDateTime endAt;
    private Long remainingSeconds;

    public static AttemptStartResponseDto from(AssessmentAttempt attempt, LocalDateTime endAt) {
        long remainingSeconds = attempt.getStartedAt() != null && endAt != null
                ? ChronoUnit.SECONDS.between(LocalDateTime.now(), endAt)
                : 0L;

        return AttemptStartResponseDto.builder()
                .attemptId(attempt.getId())
                .startedAt(attempt.getStartedAt())
                .endAt(endAt)
                .remainingSeconds(Math.max(0L, remainingSeconds))
                .build();
    }
}



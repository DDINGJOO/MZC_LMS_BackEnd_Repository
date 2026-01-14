package com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentUpdateRequestDto {

    @Size(max = 200, message = "제목은 200자 이하여야 합니다")
    private String title;

    @Size(max = 5000, message = "내용은 5000자 이하여야 합니다")
    private String content;

    private LocalDateTime startAt;

    @Min(value = 1, message = "제한 시간은 1분 이상이어야 합니다")
    private Integer durationMinutes;

    @Min(value = 0, message = "총점은 0 이상이어야 합니다")
    private BigDecimal totalScore;

    private Boolean isOnline;
    private String location;
    private String instructions;
    private Integer questionCount;
    private BigDecimal passingScore;

    /**
     * 문제 JSON (정답 포함)
     * - null이면 유지
     */
    private JsonNode questionData;
}



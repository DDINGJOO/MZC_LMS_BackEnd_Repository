package com.mzc.backend.lms.domains.board.assignment.adapter.in.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 과제 채점 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentGradeRequestDto {

    @NotNull(message = "점수는 필수입니다")
    @Min(value = 0, message = "점수는 0 이상이어야 합니다")
    private BigDecimal score;

    @Size(max = 2000, message = "피드백은 2000자 이하여야 합니다")
    private String feedback;
}

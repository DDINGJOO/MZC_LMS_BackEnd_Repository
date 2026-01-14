package com.mzc.backend.lms.domains.board.assignment.adapter.in.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 과제 수정 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentUpdateRequestDto {

    // Post 정보
    @Size(max = 200, message = "제목은 200자 이하여야 합니다")
    private String title;

    @Size(max = 10000, message = "내용은 10000자 이하여야 합니다")
    private String content;

    // Assignment 정보
    private LocalDateTime dueDate;

    @Min(value = 0, message = "만점은 0 이상이어야 합니다")
    private BigDecimal maxScore;

    private String submissionMethod;

    private Boolean lateSubmissionAllowed;

    @Min(value = 0, message = "감점율은 0 이상이어야 합니다")
    private BigDecimal latePenaltyPercent;

    @Min(value = 1, message = "최대 파일 크기는 1MB 이상이어야 합니다")
    private Integer maxFileSizeMb;

    private String allowedFileTypes;

    private String instructions;
}

package com.mzc.backend.lms.domains.board.assignment.adapter.in.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 과제 생성 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentCreateRequestDto {

    // Post 정보
    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 200, message = "제목은 200자 이하여야 합니다")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    @Size(max = 10000, message = "내용은 10000자 이하여야 합니다")
    private String content;

    private List<String> hashtags;

    private List<Long> attachmentIds;

    // Assignment 정보
    @NotNull(message = "강의 ID는 필수입니다")
    private Long courseId;

    @NotNull(message = "마감일은 필수입니다")
    private LocalDateTime dueDate;

    @NotNull(message = "만점은 필수입니다")
    @Min(value = 0, message = "만점은 0 이상이어야 합니다")
    private BigDecimal maxScore;

    @NotBlank(message = "제출 방법은 필수입니다")
    private String submissionMethod; // UPLOAD, TEXT, LINK

    @Builder.Default
    private Boolean lateSubmissionAllowed = false;

    @Min(value = 0, message = "감점율은 0 이상이어야 합니다")
    private BigDecimal latePenaltyPercent;

    @Min(value = 1, message = "최대 파일 크기는 1MB 이상이어야 합니다")
    private Integer maxFileSizeMb;

    private String allowedFileTypes; // comma-separated: "pdf,docx,hwp"

    private String instructions;
}

package com.mzc.backend.lms.domains.board.assignment.adapter.in.web.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 과제 제출 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentSubmissionRequestDto {

    @Size(max = 10000, message = "제출 내용은 10000자 이하여야 합니다")
    private String content;

    /**
     * 첨부파일 ID 목록
     */
    private List<Long> attachmentIds;
}

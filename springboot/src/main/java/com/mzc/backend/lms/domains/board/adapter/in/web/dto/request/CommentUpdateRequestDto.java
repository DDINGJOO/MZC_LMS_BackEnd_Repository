package com.mzc.backend.lms.domains.board.adapter.in.web.dto.request;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 수정 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequestDto {

    @NotBlank(message = "내용은 필수입니다")
    @Size(max = 2000, message = "댓글 내용은 2000자 이하여야 합니다")
    private String content;

    // 새로 추가할 첨부파일 ID 목록
    private List<Long> attachmentIds;

    // 삭제할 첨부파일 ID 목록
    private List<Long> removedAttachmentIds;
}

package com.mzc.backend.lms.domains.board.adapter.in.web.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 게시글 수정 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequestDto {

    private Long categoryId;

    @Size(max = 200, message = "제목은 200자 이하여야 합니다")
    private String title;

    @Size(max = 10000, message = "내용은 10000자 이하여야 합니다")
    private String content;

    private String postType;

    private Boolean isAnonymous;

    private List<Long> attachmentIds; // 새로 추가할 첨부파일 ID 목록

    private List<Long> deleteAttachmentIds; // 삭제할 첨부파일 ID 목록

    private List<String> hashtags;
}

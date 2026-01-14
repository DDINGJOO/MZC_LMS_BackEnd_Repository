package com.mzc.backend.lms.domains.board.adapter.in.web.dto.request;

import java.util.List;

import com.mzc.backend.lms.domains.board.adapter.out.persistence.enums.PostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 생성 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequestDto {

    @NotNull(message = "카테고리 ID는 필수입니다")
    private Long categoryId;

    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 200, message = "제목은 200자 이하여야 합니다")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    @Size(max = 10000, message = "내용은 10000자 이하여야 합니다")
    private String content;

    @NotNull(message = "게시글 유형은 필수입니다")
    private PostType postType;

    private Boolean isAnonymous;

    private List<Long> attachmentIds;

    private List<String> hashtags;
}

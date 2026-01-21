package com.mzc.lms.board.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequest {

    @NotNull(message = "게시글 ID는 필수입니다")
    private Long postId;

    @NotNull(message = "작성자 ID는 필수입니다")
    private Long authorId;

    private String authorName;

    @NotBlank(message = "내용은 필수입니다")
    private String content;

    private Long parentCommentId;

    private Boolean isAnonymous;
}

package com.mzc.lms.board.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostRequest {

    @NotNull(message = "게시판 ID는 필수입니다")
    private Long boardId;

    @NotNull(message = "작성자 ID는 필수입니다")
    private Long authorId;

    private String authorName;

    @NotBlank(message = "제목은 필수입니다")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    private String content;

    private Boolean isAnonymous;

    private Long parentId;
}

package com.mzc.lms.board.adapter.in.web.dto;

import com.mzc.lms.board.domain.model.BoardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBoardRequest {

    @NotBlank(message = "게시판 이름은 필수입니다")
    private String name;

    private String description;

    @NotNull(message = "게시판 유형은 필수입니다")
    private BoardType type;

    private Long courseId;

    private Boolean allowAnonymous;

    private Boolean requireApproval;
}

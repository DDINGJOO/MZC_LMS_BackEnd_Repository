package com.mzc.lms.board.adapter.in.web.dto;

import com.mzc.lms.board.domain.model.Board;
import com.mzc.lms.board.domain.model.BoardType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardResponse {

    private Long id;
    private String name;
    private String description;
    private BoardType type;
    private Long courseId;
    private Boolean isActive;
    private Boolean allowAnonymous;
    private Boolean requireApproval;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BoardResponse from(Board board) {
        return BoardResponse.builder()
                .id(board.getId())
                .name(board.getName())
                .description(board.getDescription())
                .type(board.getType())
                .courseId(board.getCourseId())
                .isActive(board.getIsActive())
                .allowAnonymous(board.getAllowAnonymous())
                .requireApproval(board.getRequireApproval())
                .displayOrder(board.getDisplayOrder())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }
}

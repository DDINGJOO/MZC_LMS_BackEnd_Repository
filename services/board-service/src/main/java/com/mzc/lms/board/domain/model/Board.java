package com.mzc.lms.board.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Board {

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

    public static Board create(String name, String description, BoardType type,
                               Long courseId, Boolean allowAnonymous, Boolean requireApproval) {
        return Board.builder()
                .name(name)
                .description(description)
                .type(type)
                .courseId(courseId)
                .isActive(true)
                .allowAnonymous(allowAnonymous != null ? allowAnonymous : false)
                .requireApproval(requireApproval != null ? requireApproval : false)
                .displayOrder(0)
                .build();
    }

    public Board update(String name, String description, Boolean allowAnonymous,
                       Boolean requireApproval, Integer displayOrder) {
        return Board.builder()
                .id(this.id)
                .name(name != null ? name : this.name)
                .description(description != null ? description : this.description)
                .type(this.type)
                .courseId(this.courseId)
                .isActive(this.isActive)
                .allowAnonymous(allowAnonymous != null ? allowAnonymous : this.allowAnonymous)
                .requireApproval(requireApproval != null ? requireApproval : this.requireApproval)
                .displayOrder(displayOrder != null ? displayOrder : this.displayOrder)
                .createdAt(this.createdAt)
                .build();
    }

    public Board activate() {
        return Board.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .type(this.type)
                .courseId(this.courseId)
                .isActive(true)
                .allowAnonymous(this.allowAnonymous)
                .requireApproval(this.requireApproval)
                .displayOrder(this.displayOrder)
                .createdAt(this.createdAt)
                .build();
    }

    public Board deactivate() {
        return Board.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .type(this.type)
                .courseId(this.courseId)
                .isActive(false)
                .allowAnonymous(this.allowAnonymous)
                .requireApproval(this.requireApproval)
                .displayOrder(this.displayOrder)
                .createdAt(this.createdAt)
                .build();
    }
}

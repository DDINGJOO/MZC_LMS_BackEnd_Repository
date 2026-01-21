package com.mzc.lms.board.adapter.out.persistence.entity;

import com.mzc.lms.board.domain.model.Board;
import com.mzc.lms.board.domain.model.BoardType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "boards",
        indexes = {
                @Index(name = "idx_board_type", columnList = "type"),
                @Index(name = "idx_board_course", columnList = "course_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BoardType type;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "allow_anonymous")
    private Boolean allowAnonymous;

    @Column(name = "require_approval")
    private Boolean requireApproval;

    @Column(name = "display_order")
    private Integer displayOrder;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static BoardEntity fromDomain(Board board) {
        return BoardEntity.builder()
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

    public Board toDomain() {
        return Board.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .type(this.type)
                .courseId(this.courseId)
                .isActive(this.isActive)
                .allowAnonymous(this.allowAnonymous)
                .requireApproval(this.requireApproval)
                .displayOrder(this.displayOrder)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}

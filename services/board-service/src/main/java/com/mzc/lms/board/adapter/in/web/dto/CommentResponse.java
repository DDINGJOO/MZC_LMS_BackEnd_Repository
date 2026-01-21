package com.mzc.lms.board.adapter.in.web.dto;

import com.mzc.lms.board.domain.model.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {

    private Long id;
    private Long postId;
    private Long authorId;
    private String authorName;
    private String content;
    private Long parentCommentId;
    private Boolean isAnonymous;
    private Boolean isDeleted;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentResponse from(Comment comment) {
        String displayAuthorName = comment.getIsAnonymous() != null && comment.getIsAnonymous()
                ? "익명" : comment.getAuthorName();
        String displayContent = comment.getIsDeleted() != null && comment.getIsDeleted()
                ? "삭제된 댓글입니다" : comment.getContent();

        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .authorId(comment.getIsAnonymous() != null && comment.getIsAnonymous() ? null : comment.getAuthorId())
                .authorName(displayAuthorName)
                .content(displayContent)
                .parentCommentId(comment.getParentCommentId())
                .isAnonymous(comment.getIsAnonymous())
                .isDeleted(comment.getIsDeleted())
                .likeCount(comment.getLikeCount())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}

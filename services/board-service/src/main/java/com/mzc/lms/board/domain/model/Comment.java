package com.mzc.lms.board.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Comment {

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

    public static Comment create(Long postId, Long authorId, String authorName,
                                 String content, Long parentCommentId, Boolean isAnonymous) {
        return Comment.builder()
                .postId(postId)
                .authorId(authorId)
                .authorName(authorName)
                .content(content)
                .parentCommentId(parentCommentId)
                .isAnonymous(isAnonymous != null ? isAnonymous : false)
                .isDeleted(false)
                .likeCount(0)
                .build();
    }

    public Comment update(String content) {
        return Comment.builder()
                .id(this.id)
                .postId(this.postId)
                .authorId(this.authorId)
                .authorName(this.authorName)
                .content(content)
                .parentCommentId(this.parentCommentId)
                .isAnonymous(this.isAnonymous)
                .isDeleted(this.isDeleted)
                .likeCount(this.likeCount)
                .createdAt(this.createdAt)
                .build();
    }

    public Comment delete() {
        return Comment.builder()
                .id(this.id)
                .postId(this.postId)
                .authorId(this.authorId)
                .authorName(this.authorName)
                .content(this.content)
                .parentCommentId(this.parentCommentId)
                .isAnonymous(this.isAnonymous)
                .isDeleted(true)
                .likeCount(this.likeCount)
                .createdAt(this.createdAt)
                .build();
    }

    public Comment incrementLikeCount() {
        return Comment.builder()
                .id(this.id)
                .postId(this.postId)
                .authorId(this.authorId)
                .authorName(this.authorName)
                .content(this.content)
                .parentCommentId(this.parentCommentId)
                .isAnonymous(this.isAnonymous)
                .isDeleted(this.isDeleted)
                .likeCount(this.likeCount + 1)
                .createdAt(this.createdAt)
                .build();
    }

    public boolean isReply() {
        return this.parentCommentId != null;
    }
}

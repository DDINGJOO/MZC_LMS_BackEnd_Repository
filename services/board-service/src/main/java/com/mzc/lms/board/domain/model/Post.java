package com.mzc.lms.board.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Post {

    private Long id;
    private Long boardId;
    private Long authorId;
    private String authorName;
    private String title;
    private String content;
    private PostStatus status;
    private Boolean isPinned;
    private Boolean isAnonymous;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;

    public static Post create(Long boardId, Long authorId, String authorName,
                              String title, String content, Boolean isAnonymous, Long parentId) {
        return Post.builder()
                .boardId(boardId)
                .authorId(authorId)
                .authorName(authorName)
                .title(title)
                .content(content)
                .status(PostStatus.DRAFT)
                .isPinned(false)
                .isAnonymous(isAnonymous != null ? isAnonymous : false)
                .viewCount(0)
                .likeCount(0)
                .commentCount(0)
                .parentId(parentId)
                .build();
    }

    public Post update(String title, String content) {
        return Post.builder()
                .id(this.id)
                .boardId(this.boardId)
                .authorId(this.authorId)
                .authorName(this.authorName)
                .title(title != null ? title : this.title)
                .content(content != null ? content : this.content)
                .status(this.status)
                .isPinned(this.isPinned)
                .isAnonymous(this.isAnonymous)
                .viewCount(this.viewCount)
                .likeCount(this.likeCount)
                .commentCount(this.commentCount)
                .parentId(this.parentId)
                .createdAt(this.createdAt)
                .publishedAt(this.publishedAt)
                .build();
    }

    public Post publish(boolean requireApproval) {
        PostStatus newStatus = requireApproval ? PostStatus.PENDING : PostStatus.PUBLISHED;
        LocalDateTime publishTime = requireApproval ? null : LocalDateTime.now();

        return Post.builder()
                .id(this.id)
                .boardId(this.boardId)
                .authorId(this.authorId)
                .authorName(this.authorName)
                .title(this.title)
                .content(this.content)
                .status(newStatus)
                .isPinned(this.isPinned)
                .isAnonymous(this.isAnonymous)
                .viewCount(this.viewCount)
                .likeCount(this.likeCount)
                .commentCount(this.commentCount)
                .parentId(this.parentId)
                .createdAt(this.createdAt)
                .publishedAt(publishTime)
                .build();
    }

    public Post approve() {
        return Post.builder()
                .id(this.id)
                .boardId(this.boardId)
                .authorId(this.authorId)
                .authorName(this.authorName)
                .title(this.title)
                .content(this.content)
                .status(PostStatus.PUBLISHED)
                .isPinned(this.isPinned)
                .isAnonymous(this.isAnonymous)
                .viewCount(this.viewCount)
                .likeCount(this.likeCount)
                .commentCount(this.commentCount)
                .parentId(this.parentId)
                .createdAt(this.createdAt)
                .publishedAt(LocalDateTime.now())
                .build();
    }

    public Post hide() {
        return withStatus(PostStatus.HIDDEN);
    }

    public Post delete() {
        return withStatus(PostStatus.DELETED);
    }

    public Post pin() {
        return Post.builder()
                .id(this.id)
                .boardId(this.boardId)
                .authorId(this.authorId)
                .authorName(this.authorName)
                .title(this.title)
                .content(this.content)
                .status(this.status)
                .isPinned(true)
                .isAnonymous(this.isAnonymous)
                .viewCount(this.viewCount)
                .likeCount(this.likeCount)
                .commentCount(this.commentCount)
                .parentId(this.parentId)
                .createdAt(this.createdAt)
                .publishedAt(this.publishedAt)
                .build();
    }

    public Post unpin() {
        return Post.builder()
                .id(this.id)
                .boardId(this.boardId)
                .authorId(this.authorId)
                .authorName(this.authorName)
                .title(this.title)
                .content(this.content)
                .status(this.status)
                .isPinned(false)
                .isAnonymous(this.isAnonymous)
                .viewCount(this.viewCount)
                .likeCount(this.likeCount)
                .commentCount(this.commentCount)
                .parentId(this.parentId)
                .createdAt(this.createdAt)
                .publishedAt(this.publishedAt)
                .build();
    }

    public Post incrementViewCount() {
        return Post.builder()
                .id(this.id)
                .boardId(this.boardId)
                .authorId(this.authorId)
                .authorName(this.authorName)
                .title(this.title)
                .content(this.content)
                .status(this.status)
                .isPinned(this.isPinned)
                .isAnonymous(this.isAnonymous)
                .viewCount(this.viewCount + 1)
                .likeCount(this.likeCount)
                .commentCount(this.commentCount)
                .parentId(this.parentId)
                .createdAt(this.createdAt)
                .publishedAt(this.publishedAt)
                .build();
    }

    private Post withStatus(PostStatus status) {
        return Post.builder()
                .id(this.id)
                .boardId(this.boardId)
                .authorId(this.authorId)
                .authorName(this.authorName)
                .title(this.title)
                .content(this.content)
                .status(status)
                .isPinned(this.isPinned)
                .isAnonymous(this.isAnonymous)
                .viewCount(this.viewCount)
                .likeCount(this.likeCount)
                .commentCount(this.commentCount)
                .parentId(this.parentId)
                .createdAt(this.createdAt)
                .publishedAt(this.publishedAt)
                .build();
    }
}

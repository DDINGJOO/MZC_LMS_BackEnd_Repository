package com.mzc.lms.board.adapter.in.web.dto;

import com.mzc.lms.board.domain.model.Post;
import com.mzc.lms.board.domain.model.PostStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostResponse {

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

    public static PostResponse from(Post post) {
        String displayAuthorName = post.getIsAnonymous() != null && post.getIsAnonymous()
                ? "익명" : post.getAuthorName();

        return PostResponse.builder()
                .id(post.getId())
                .boardId(post.getBoardId())
                .authorId(post.getIsAnonymous() != null && post.getIsAnonymous() ? null : post.getAuthorId())
                .authorName(displayAuthorName)
                .title(post.getTitle())
                .content(post.getContent())
                .status(post.getStatus())
                .isPinned(post.getIsPinned())
                .isAnonymous(post.getIsAnonymous())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .parentId(post.getParentId())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .publishedAt(post.getPublishedAt())
                .build();
    }
}

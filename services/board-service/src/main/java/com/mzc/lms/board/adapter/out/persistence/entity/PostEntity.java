package com.mzc.lms.board.adapter.out.persistence.entity;

import com.mzc.lms.board.domain.model.Post;
import com.mzc.lms.board.domain.model.PostStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts",
        indexes = {
                @Index(name = "idx_post_board", columnList = "board_id"),
                @Index(name = "idx_post_author", columnList = "author_id"),
                @Index(name = "idx_post_status", columnList = "status"),
                @Index(name = "idx_post_pinned", columnList = "is_pinned")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(name = "author_name", length = 100)
    private String authorName;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PostStatus status;

    @Column(name = "is_pinned")
    private Boolean isPinned;

    @Column(name = "is_anonymous")
    private Boolean isAnonymous;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "comment_count")
    private Integer commentCount;

    @Column(name = "parent_id")
    private Long parentId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    public static PostEntity fromDomain(Post post) {
        return PostEntity.builder()
                .id(post.getId())
                .boardId(post.getBoardId())
                .authorId(post.getAuthorId())
                .authorName(post.getAuthorName())
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

    public Post toDomain() {
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
                .viewCount(this.viewCount)
                .likeCount(this.likeCount)
                .commentCount(this.commentCount)
                .parentId(this.parentId)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .publishedAt(this.publishedAt)
                .build();
    }
}

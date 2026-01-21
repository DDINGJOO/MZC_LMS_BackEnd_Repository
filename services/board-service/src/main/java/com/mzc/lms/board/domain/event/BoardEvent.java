package com.mzc.lms.board.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardEvent {

    private String eventId;
    private String eventType;
    private Long boardId;
    private Long postId;
    private Long commentId;
    private Long authorId;
    private String title;
    private LocalDateTime timestamp;

    public static BoardEvent postCreated(Long postId, Long boardId, Long authorId, String title) {
        return BoardEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("POST_CREATED")
                .postId(postId)
                .boardId(boardId)
                .authorId(authorId)
                .title(title)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static BoardEvent postPublished(Long postId, Long boardId, Long authorId, String title) {
        return BoardEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("POST_PUBLISHED")
                .postId(postId)
                .boardId(boardId)
                .authorId(authorId)
                .title(title)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static BoardEvent commentCreated(Long commentId, Long postId, Long authorId) {
        return BoardEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("COMMENT_CREATED")
                .commentId(commentId)
                .postId(postId)
                .authorId(authorId)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static BoardEvent postLiked(Long postId, Long userId) {
        return BoardEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("POST_LIKED")
                .postId(postId)
                .authorId(userId)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

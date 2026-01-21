package com.mzc.lms.board.application.port.in;

import com.mzc.lms.board.domain.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentUseCase {

    Comment createComment(Long postId, Long authorId, String authorName,
                          String content, Long parentCommentId, Boolean isAnonymous);

    Comment updateComment(Long id, String content);

    Comment deleteComment(Long id);

    Comment likeComment(Long id);

    Optional<Comment> findById(Long id);

    List<Comment> findByPostId(Long postId);

    List<Comment> findByAuthorId(Long authorId);

    Integer countByPostId(Long postId);
}

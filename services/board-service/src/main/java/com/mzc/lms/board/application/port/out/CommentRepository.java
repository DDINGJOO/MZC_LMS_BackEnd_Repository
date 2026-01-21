package com.mzc.lms.board.application.port.out;

import com.mzc.lms.board.domain.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findById(Long id);

    List<Comment> findByPostId(Long postId);

    List<Comment> findByAuthorId(Long authorId);

    Integer countByPostId(Long postId);

    void deleteById(Long id);
}

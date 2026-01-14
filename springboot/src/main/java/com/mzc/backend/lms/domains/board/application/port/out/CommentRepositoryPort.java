package com.mzc.backend.lms.domains.board.application.port.out;

import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Comment;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Post;

import java.util.List;
import java.util.Optional;

/**
 * Comment 영속성을 위한 Port
 */
public interface CommentRepositoryPort {

    /**
     * 댓글 저장
     */
    Comment save(Comment comment);

    /**
     * ID로 댓글 조회
     */
    Optional<Comment> findById(Long id);

    /**
     * 게시글의 댓글 목록 조회
     */
    List<Comment> findByPost(Post post);

    /**
     * 부모 댓글의 자식 댓글 목록 조회
     */
    List<Comment> findByParentComment(Comment parentComment);
}

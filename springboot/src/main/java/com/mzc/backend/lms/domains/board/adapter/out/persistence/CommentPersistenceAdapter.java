package com.mzc.backend.lms.domains.board.adapter.out.persistence;

import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Comment;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Post;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.repository.CommentRepositoryJpa;
import com.mzc.backend.lms.domains.board.application.port.out.CommentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Comment 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements CommentRepositoryPort {

    private final CommentRepositoryJpa commentRepositoryJpa;

    @Override
    public Comment save(Comment comment) {
        return commentRepositoryJpa.save(comment);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepositoryJpa.findById(id);
    }

    @Override
    public List<Comment> findByPost(Post post) {
        return commentRepositoryJpa.findByPost(post);
    }

    @Override
    public List<Comment> findByParentComment(Comment parentComment) {
        return commentRepositoryJpa.findByParentComment(parentComment);
    }
}

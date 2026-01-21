package com.mzc.lms.board.adapter.out.persistence;

import com.mzc.lms.board.adapter.out.persistence.entity.CommentEntity;
import com.mzc.lms.board.adapter.out.persistence.repository.CommentJpaRepository;
import com.mzc.lms.board.application.port.out.CommentRepository;
import com.mzc.lms.board.domain.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Comment save(Comment comment) {
        CommentEntity entity = CommentEntity.fromDomain(comment);
        CommentEntity savedEntity = commentJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentJpaRepository.findById(id).map(CommentEntity::toDomain);
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return commentJpaRepository.findByPostIdOrderByCreatedAtAsc(postId).stream()
                .map(CommentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Comment> findByAuthorId(Long authorId) {
        return commentJpaRepository.findByAuthorIdOrderByCreatedAtDesc(authorId).stream()
                .map(CommentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Integer countByPostId(Long postId) {
        return commentJpaRepository.countByPostIdAndIsDeletedFalse(postId);
    }

    @Override
    public void deleteById(Long id) {
        commentJpaRepository.deleteById(id);
    }
}

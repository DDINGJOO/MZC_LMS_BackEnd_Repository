package com.mzc.lms.board.adapter.out.persistence.repository;

import com.mzc.lms.board.adapter.out.persistence.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByPostIdOrderByCreatedAtAsc(Long postId);

    List<CommentEntity> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    Integer countByPostIdAndIsDeletedFalse(Long postId);
}

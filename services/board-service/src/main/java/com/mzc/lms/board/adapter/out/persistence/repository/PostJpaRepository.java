package com.mzc.lms.board.adapter.out.persistence.repository;

import com.mzc.lms.board.adapter.out.persistence.entity.PostEntity;
import com.mzc.lms.board.domain.model.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {

    Page<PostEntity> findByBoardIdOrderByCreatedAtDesc(Long boardId, Pageable pageable);

    Page<PostEntity> findByBoardIdAndStatusOrderByCreatedAtDesc(Long boardId, PostStatus status, Pageable pageable);

    Page<PostEntity> findByAuthorIdOrderByCreatedAtDesc(Long authorId, Pageable pageable);

    List<PostEntity> findByBoardIdAndIsPinnedTrueOrderByCreatedAtDesc(Long boardId);

    @Query("SELECT p FROM PostEntity p WHERE p.boardId = :boardId AND " +
           "(p.title LIKE %:keyword% OR p.content LIKE %:keyword%) " +
           "ORDER BY p.createdAt DESC")
    Page<PostEntity> searchByBoardIdAndKeyword(@Param("boardId") Long boardId,
                                               @Param("keyword") String keyword,
                                               Pageable pageable);
}

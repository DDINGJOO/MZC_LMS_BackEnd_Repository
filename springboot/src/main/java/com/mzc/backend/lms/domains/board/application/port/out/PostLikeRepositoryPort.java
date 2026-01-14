package com.mzc.backend.lms.domains.board.application.port.out;

import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.PostLike;

import java.util.List;
import java.util.Optional;

/**
 * PostLike 영속성을 위한 Port
 */
public interface PostLikeRepositoryPort {

    /**
     * 좋아요 저장
     */
    PostLike save(PostLike postLike);

    /**
     * ID로 좋아요 조회
     */
    Optional<PostLike> findById(Long id);

    /**
     * 사용자의 특정 게시글 좋아요 조회
     */
    Optional<PostLike> findByUserIdAndPostId(Long userId, Long postId);

    /**
     * 사용자의 특정 게시글 좋아요 여부 확인
     */
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    /**
     * 사용자가 좋아요한 게시글 ID 목록 조회
     */
    List<Long> findLikedPostIdsByUserIdAndPostIds(Long userId, List<Long> postIds);

    /**
     * 게시글의 좋아요 수 카운트
     */
    long countByPostId(Long postId);

    /**
     * 게시글의 모든 좋아요 삭제
     */
    void deleteByPostId(Long postId);

    /**
     * 좋아요 삭제
     */
    void delete(PostLike postLike);
}

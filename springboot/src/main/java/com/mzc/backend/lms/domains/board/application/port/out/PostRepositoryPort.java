package com.mzc.backend.lms.domains.board.application.port.out;

import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.BoardCategory;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Post 영속성을 위한 Port
 */
public interface PostRepositoryPort {

    /**
     * 게시글 저장
     */
    Post save(Post post);

    /**
     * ID로 게시글 조회
     */
    Optional<Post> findById(Long id);

    /**
     * ID로 게시글 조회 (모든 연관관계 즉시 로딩)
     */
    Optional<Post> findWithAllById(Long id);

    /**
     * 카테고리로 게시글 목록 조회
     */
    List<Post> findByCategory(BoardCategory category);

    /**
     * 카테고리로 게시글 페이징 조회
     */
    Page<Post> findByCategory(BoardCategory category, Pageable pageable);

    /**
     * 제목으로 게시글 검색 (페이징)
     */
    Page<Post> findByTitleContaining(String title, Pageable pageable);

    /**
     * 카테고리와 제목으로 게시글 검색 (페이징)
     */
    Page<Post> findByCategoryAndTitleContaining(BoardCategory category, String title, Pageable pageable);

    /**
     * 카테고리와 해시태그로 게시글 검색 (페이징)
     */
    Page<Post> findByCategoryAndHashtagName(BoardCategory category, String hashtagName, Pageable pageable);

    /**
     * 카테고리, 제목, 해시태그로 게시글 검색 (페이징)
     */
    Page<Post> findByCategoryAndTitleContainingAndHashtagName(BoardCategory category, String title, String hashtagName, Pageable pageable);

    /**
     * 카테고리와 학과 ID로 게시글 검색 (페이징)
     */
    Page<Post> findByCategoryAndDepartmentId(BoardCategory category, Long departmentId, Pageable pageable);

    /**
     * 카테고리, 학과 ID, 제목으로 게시글 검색 (페이징)
     */
    Page<Post> findByCategoryAndDepartmentIdAndTitleContaining(BoardCategory category, Long departmentId, String title, Pageable pageable);

    /**
     * 게시글 상세 조회 (해시태그 포함)
     */
    Optional<Post> findByIdWithHashtags(Long postId);

    /**
     * 전체 게시글 페이징 조회
     */
    Page<Post> findAll(Pageable pageable);
}

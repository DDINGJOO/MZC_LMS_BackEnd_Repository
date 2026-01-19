package com.mzc.backend.lms.domains.board.adapter.out.persistence.specification;

import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.BoardCategory;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Post;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.enums.PostType;
import org.springframework.data.jpa.domain.Specification;

/**
 * Post 엔티티용 Specification 클래스
 * 동적 쿼리 조건을 재사용 가능한 형태로 제공
 */
public final class PostSpecifications {

    private PostSpecifications() {
        // 유틸리티 클래스 - 인스턴스화 방지
    }

    /**
     * 삭제되지 않은 게시글만 조회
     */
    public static Specification<Post> notDeleted() {
        return (root, query, cb) -> cb.equal(root.get("isDeleted"), false);
    }

    /**
     * 카테고리로 필터링
     */
    public static Specification<Post> byCategory(BoardCategory category) {
        return (root, query, cb) ->
                category == null ? null : cb.equal(root.get("category"), category);
    }

    /**
     * 카테고리 ID로 필터링
     */
    public static Specification<Post> byCategoryId(Long categoryId) {
        return (root, query, cb) ->
                categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }

    /**
     * 제목 검색 (부분 일치)
     */
    public static Specification<Post> titleContains(String keyword) {
        return (root, query, cb) ->
                keyword == null || keyword.isBlank() ? null : cb.like(root.get("title"), "%" + keyword + "%");
    }

    /**
     * 내용 검색 (부분 일치)
     */
    public static Specification<Post> contentContains(String keyword) {
        return (root, query, cb) ->
                keyword == null || keyword.isBlank() ? null : cb.like(root.get("content"), "%" + keyword + "%");
    }

    /**
     * 제목 또는 내용 검색 (부분 일치)
     */
    public static Specification<Post> titleOrContentContains(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }
            String pattern = "%" + keyword + "%";
            return cb.or(
                    cb.like(root.get("title"), pattern),
                    cb.like(root.get("content"), pattern)
            );
        };
    }

    /**
     * 작성자 ID로 필터링
     */
    public static Specification<Post> byAuthorId(Long authorId) {
        return (root, query, cb) ->
                authorId == null ? null : cb.equal(root.get("authorId"), authorId);
    }

    /**
     * 학과 ID로 필터링
     */
    public static Specification<Post> byDepartmentId(Long departmentId) {
        return (root, query, cb) ->
                departmentId == null ? null : cb.equal(root.get("departmentId"), departmentId);
    }

    /**
     * 강의 ID로 필터링
     */
    public static Specification<Post> byCourseId(Long courseId) {
        return (root, query, cb) ->
                courseId == null ? null : cb.equal(root.get("courseId"), courseId);
    }

    /**
     * 게시글 유형으로 필터링
     */
    public static Specification<Post> byPostType(PostType postType) {
        return (root, query, cb) ->
                postType == null ? null : cb.equal(root.get("postType"), postType);
    }

    /**
     * 익명 게시글 여부로 필터링
     */
    public static Specification<Post> isAnonymous(Boolean anonymous) {
        return (root, query, cb) ->
                anonymous == null ? null : cb.equal(root.get("isAnonymous"), anonymous);
    }

    /**
     * 좋아요 수 이상 필터링
     */
    public static Specification<Post> likesGreaterThanOrEqual(Integer minLikes) {
        return (root, query, cb) ->
                minLikes == null ? null : cb.greaterThanOrEqualTo(root.get("likeCount"), minLikes);
    }

    /**
     * 조회수 이상 필터링
     */
    public static Specification<Post> viewsGreaterThanOrEqual(Integer minViews) {
        return (root, query, cb) ->
                minViews == null ? null : cb.greaterThanOrEqualTo(root.get("viewCount"), minViews);
    }
}

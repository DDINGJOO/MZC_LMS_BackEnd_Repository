package com.mzc.backend.lms.domains.board.assignment.adapter.out.persistence.specification;

import com.mzc.backend.lms.domains.board.assignment.adapter.out.persistence.entity.Assignment;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Assignment 엔티티용 Specification 클래스
 */
public final class AssignmentSpecifications {

    private AssignmentSpecifications() {
    }

    public static Specification<Assignment> notDeleted() {
        return (root, query, cb) -> cb.equal(root.get("isDeleted"), false);
    }

    public static Specification<Assignment> byCourseId(Long courseId) {
        return (root, query, cb) ->
                courseId == null ? null : cb.equal(root.get("courseId"), courseId);
    }

    public static Specification<Assignment> byPostId(Long postId) {
        return (root, query, cb) ->
                postId == null ? null : cb.equal(root.get("post").get("id"), postId);
    }

    public static Specification<Assignment> byCreatorId(Long creatorId) {
        return (root, query, cb) ->
                creatorId == null ? null : cb.equal(root.get("createdBy"), creatorId);
    }

    public static Specification<Assignment> dueDateBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start == null && end == null) {
                return null;
            }
            if (start != null && end != null) {
                return cb.between(root.get("dueDate"), start, end);
            }
            if (start != null) {
                return cb.greaterThanOrEqualTo(root.get("dueDate"), start);
            }
            return cb.lessThanOrEqualTo(root.get("dueDate"), end);
        };
    }

    public static Specification<Assignment> overdue(LocalDateTime now) {
        return (root, query, cb) ->
                now == null ? null : cb.lessThan(root.get("dueDate"), now);
    }

    public static Specification<Assignment> upcoming(LocalDateTime now) {
        return (root, query, cb) ->
                now == null ? null : cb.greaterThan(root.get("dueDate"), now);
    }

    public static Specification<Assignment> lateSubmissionAllowed(Boolean allowed) {
        return (root, query, cb) ->
                allowed == null ? null : cb.equal(root.get("lateSubmissionAllowed"), allowed);
    }
}

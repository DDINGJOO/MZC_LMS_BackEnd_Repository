package com.mzc.backend.lms.domains.assessment.adapter.out.persistence.specification;

import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.Assessment;
import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.enums.AssessmentType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Assessment 엔티티용 Specification 클래스
 */
public final class AssessmentSpecifications {

    private AssessmentSpecifications() {
    }

    public static Specification<Assessment> notDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    public static Specification<Assessment> byCourseId(Long courseId) {
        return (root, query, cb) ->
                courseId == null ? null : cb.equal(root.get("courseId"), courseId);
    }

    public static Specification<Assessment> byType(AssessmentType type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<Assessment> startedBefore(LocalDateTime dateTime) {
        return (root, query, cb) ->
                dateTime == null ? null : cb.lessThanOrEqualTo(root.get("startAt"), dateTime);
    }

    public static Specification<Assessment> endedAfter(LocalDateTime dateTime) {
        return (root, query, cb) ->
                dateTime == null ? null : cb.greaterThanOrEqualTo(root.get("endAt"), dateTime);
    }

    public static Specification<Assessment> isActive(LocalDateTime now) {
        return (root, query, cb) -> {
            if (now == null) {
                return null;
            }
            return cb.and(
                    cb.lessThanOrEqualTo(root.get("startAt"), now),
                    cb.greaterThanOrEqualTo(root.get("endAt"), now)
            );
        };
    }
}

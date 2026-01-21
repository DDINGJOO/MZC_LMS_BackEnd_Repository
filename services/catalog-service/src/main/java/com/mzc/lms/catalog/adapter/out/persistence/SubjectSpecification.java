package com.mzc.lms.catalog.adapter.out.persistence;

import com.mzc.lms.catalog.adapter.out.persistence.entity.SubjectEntity;
import org.springframework.data.jpa.domain.Specification;

public class SubjectSpecification {

    public static Specification<SubjectEntity> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("subjectCode")), pattern),
                    cb.like(cb.lower(root.get("subjectName")), pattern),
                    cb.like(cb.lower(root.get("subjectDescription")), pattern)
            );
        };
    }

    public static Specification<SubjectEntity> hasDepartmentId(Long departmentId) {
        return (root, query, cb) -> {
            if (departmentId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("departmentId"), departmentId);
        };
    }

    public static Specification<SubjectEntity> hasCourseTypeId(Long courseTypeId) {
        return (root, query, cb) -> {
            if (courseTypeId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("courseTypeId"), courseTypeId);
        };
    }

    public static Specification<SubjectEntity> hasCredits(Integer credits) {
        return (root, query, cb) -> {
            if (credits == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("credits"), credits);
        };
    }

    public static Specification<SubjectEntity> isActive(Boolean isActive) {
        return (root, query, cb) -> {
            if (isActive == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("isActive"), isActive);
        };
    }
}

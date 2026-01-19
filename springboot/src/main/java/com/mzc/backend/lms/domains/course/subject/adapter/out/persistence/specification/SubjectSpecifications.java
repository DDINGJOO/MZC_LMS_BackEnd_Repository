package com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.specification;

import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.entity.Subject;
import org.springframework.data.jpa.domain.Specification;

/**
 * Subject 엔티티용 Specification 클래스
 */
public final class SubjectSpecifications {

    private SubjectSpecifications() {
    }

    public static Specification<Subject> byDepartmentId(Long departmentId) {
        return (root, query, cb) ->
                departmentId == null ? null : cb.equal(root.get("department").get("id"), departmentId);
    }

    public static Specification<Subject> byCourseTypeId(Long courseTypeId) {
        return (root, query, cb) ->
                courseTypeId == null ? null : cb.equal(root.get("courseType").get("id"), courseTypeId);
    }

    public static Specification<Subject> byCredits(Integer credits) {
        return (root, query, cb) ->
                credits == null ? null : cb.equal(root.get("credits"), credits);
    }

    public static Specification<Subject> bySubjectCode(String subjectCode) {
        return (root, query, cb) ->
                subjectCode == null || subjectCode.isBlank() ? null : cb.equal(root.get("subjectCode"), subjectCode);
    }

    public static Specification<Subject> subjectNameContains(String keyword) {
        return (root, query, cb) ->
                keyword == null || keyword.isBlank() ? null : cb.like(cb.lower(root.get("subjectName")), "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<Subject> subjectCodeContains(String keyword) {
        return (root, query, cb) ->
                keyword == null || keyword.isBlank() ? null : cb.like(cb.lower(root.get("subjectCode")), "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<Subject> searchByKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("subjectName")), pattern),
                    cb.like(cb.lower(root.get("subjectCode")), pattern)
            );
        };
    }
}

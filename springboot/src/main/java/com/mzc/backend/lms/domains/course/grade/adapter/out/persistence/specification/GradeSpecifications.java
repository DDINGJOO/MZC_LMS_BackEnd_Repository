package com.mzc.backend.lms.domains.course.grade.adapter.out.persistence.specification;

import com.mzc.backend.lms.domains.course.grade.adapter.out.persistence.entity.Grade;
import com.mzc.backend.lms.domains.course.grade.domain.enums.GradeStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Grade 엔티티용 Specification 클래스
 */
public final class GradeSpecifications {

    private GradeSpecifications() {
    }

    public static Specification<Grade> byCourseId(Long courseId) {
        return (root, query, cb) ->
                courseId == null ? null : cb.equal(root.get("courseId"), courseId);
    }

    public static Specification<Grade> byStudentId(Long studentId) {
        return (root, query, cb) ->
                studentId == null ? null : cb.equal(root.get("studentId"), studentId);
    }

    public static Specification<Grade> byStudentIds(List<Long> studentIds) {
        return (root, query, cb) ->
                studentIds == null || studentIds.isEmpty() ? null : root.get("studentId").in(studentIds);
    }

    public static Specification<Grade> byAcademicTermId(Long academicTermId) {
        return (root, query, cb) ->
                academicTermId == null ? null : cb.equal(root.get("academicTermId"), academicTermId);
    }

    public static Specification<Grade> byStatus(GradeStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Grade> published() {
        return (root, query, cb) -> cb.equal(root.get("status"), GradeStatus.PUBLISHED);
    }

    public static Specification<Grade> hasFinalGrade() {
        return (root, query, cb) -> cb.isNotNull(root.get("finalGrade"));
    }

    public static Specification<Grade> byFinalGrade(String finalGrade) {
        return (root, query, cb) ->
                finalGrade == null || finalGrade.isBlank() ? null : cb.equal(root.get("finalGrade"), finalGrade);
    }
}

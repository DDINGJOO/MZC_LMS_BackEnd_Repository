package com.mzc.lms.course.adapter.out.persistence;

import com.mzc.lms.course.adapter.out.persistence.entity.CourseEntity;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecification {

    public static Specification<CourseEntity> hasSubjectId(Long subjectId) {
        return (root, query, cb) -> {
            if (subjectId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("subjectId"), subjectId);
        };
    }

    public static Specification<CourseEntity> hasProfessorId(Long professorId) {
        return (root, query, cb) -> {
            if (professorId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("professorId"), professorId);
        };
    }

    public static Specification<CourseEntity> hasAcademicTermId(Long academicTermId) {
        return (root, query, cb) -> {
            if (academicTermId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("academicTermId"), academicTermId);
        };
    }

    public static Specification<CourseEntity> isActive(Boolean isActive) {
        return (root, query, cb) -> {
            if (isActive == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<CourseEntity> hasAvailableSeats(Boolean hasAvailableSeats) {
        return (root, query, cb) -> {
            if (hasAvailableSeats == null || !hasAvailableSeats) {
                return cb.conjunction();
            }
            return cb.lessThan(root.get("currentStudents"), root.get("maxStudents"));
        };
    }
}

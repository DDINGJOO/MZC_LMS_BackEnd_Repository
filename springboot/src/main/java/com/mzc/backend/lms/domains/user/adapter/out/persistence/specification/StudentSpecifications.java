package com.mzc.backend.lms.domains.user.adapter.out.persistence.specification;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Student;
import org.springframework.data.jpa.domain.Specification;

/**
 * Student 엔티티용 Specification 클래스
 * 동적 쿼리 조건을 재사용 가능한 형태로 제공
 */
public final class StudentSpecifications {

    private StudentSpecifications() {
        // 유틸리티 클래스 - 인스턴스화 방지
    }

    /**
     * 입학년도로 필터링
     */
    public static Specification<Student> byAdmissionYear(Integer admissionYear) {
        return (root, query, cb) ->
                admissionYear == null ? null : cb.equal(root.get("admissionYear"), admissionYear);
    }

    /**
     * 학년으로 필터링
     */
    public static Specification<Student> byGrade(Integer grade) {
        return (root, query, cb) ->
                grade == null ? null : cb.equal(root.get("grade"), grade);
    }

    /**
     * 입학년도 범위로 필터링
     */
    public static Specification<Student> admissionYearBetween(Integer startYear, Integer endYear) {
        return (root, query, cb) -> {
            if (startYear == null && endYear == null) {
                return null;
            }
            if (startYear != null && endYear != null) {
                return cb.between(root.get("admissionYear"), startYear, endYear);
            }
            if (startYear != null) {
                return cb.greaterThanOrEqualTo(root.get("admissionYear"), startYear);
            }
            return cb.lessThanOrEqualTo(root.get("admissionYear"), endYear);
        };
    }

    /**
     * 학번 패턴으로 검색 (부분 일치)
     */
    public static Specification<Student> studentIdLike(String pattern) {
        return (root, query, cb) ->
                pattern == null || pattern.isBlank() ? null :
                        cb.like(root.get("studentId").as(String.class), "%" + pattern + "%");
    }

    /**
     * 여러 학번으로 필터링
     */
    public static Specification<Student> byStudentIds(java.util.List<Long> studentIds) {
        return (root, query, cb) ->
                studentIds == null || studentIds.isEmpty() ? null : root.get("studentId").in(studentIds);
    }

    /**
     * 학과 ID로 필터링 (StudentDepartment 조인)
     */
    public static Specification<Student> byDepartmentId(Long departmentId) {
        return (root, query, cb) ->
                departmentId == null ? null :
                        cb.equal(root.get("studentDepartment").get("department").get("id"), departmentId);
    }

    /**
     * 사용자 삭제되지 않은 학생만 조회
     */
    public static Specification<Student> userNotDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("user").get("deletedAt"));
    }
}

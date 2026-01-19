package com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.specification;

import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity.Enrollment;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Enrollment 엔티티용 Specification 클래스
 * 동적 쿼리 조건을 재사용 가능한 형태로 제공
 */
public final class EnrollmentSpecifications {

    private EnrollmentSpecifications() {
        // 유틸리티 클래스 - 인스턴스화 방지
    }

    /**
     * 학생 ID로 필터링
     */
    public static Specification<Enrollment> byStudentId(Long studentId) {
        return (root, query, cb) ->
                studentId == null ? null : cb.equal(root.get("studentId"), studentId);
    }

    /**
     * 강의 ID로 필터링
     */
    public static Specification<Enrollment> byCourseId(Long courseId) {
        return (root, query, cb) ->
                courseId == null ? null : cb.equal(root.get("courseId"), courseId);
    }

    /**
     * 수강신청 기간 내 필터링 (이후)
     */
    public static Specification<Enrollment> enrolledAfter(LocalDateTime dateTime) {
        return (root, query, cb) ->
                dateTime == null ? null : cb.greaterThanOrEqualTo(root.get("enrolledAt"), dateTime);
    }

    /**
     * 수강신청 기간 내 필터링 (이전)
     */
    public static Specification<Enrollment> enrolledBefore(LocalDateTime dateTime) {
        return (root, query, cb) ->
                dateTime == null ? null : cb.lessThanOrEqualTo(root.get("enrolledAt"), dateTime);
    }

    /**
     * 여러 강의 ID로 필터링
     */
    public static Specification<Enrollment> byCourseIds(java.util.List<Long> courseIds) {
        return (root, query, cb) ->
                courseIds == null || courseIds.isEmpty() ? null : root.get("courseId").in(courseIds);
    }

    /**
     * 여러 학생 ID로 필터링
     */
    public static Specification<Enrollment> byStudentIds(java.util.List<Long> studentIds) {
        return (root, query, cb) ->
                studentIds == null || studentIds.isEmpty() ? null : root.get("studentId").in(studentIds);
    }
}

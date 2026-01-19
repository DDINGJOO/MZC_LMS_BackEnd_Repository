package com.mzc.backend.lms.domains.user.adapter.out.persistence.specification;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Professor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

/**
 * Professor 엔티티용 Specification 클래스
 * 동적 쿼리 조건을 재사용 가능한 형태로 제공
 */
public final class ProfessorSpecifications {

    private ProfessorSpecifications() {
        // 유틸리티 클래스 - 인스턴스화 방지
    }

    /**
     * 임용일자 범위로 필터링
     */
    public static Specification<Professor> appointmentDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null) {
                return null;
            }
            if (startDate != null && endDate != null) {
                return cb.between(root.get("appointmentDate"), startDate, endDate);
            }
            if (startDate != null) {
                return cb.greaterThanOrEqualTo(root.get("appointmentDate"), startDate);
            }
            return cb.lessThanOrEqualTo(root.get("appointmentDate"), endDate);
        };
    }

    /**
     * 임용일자 이후 필터링
     */
    public static Specification<Professor> appointmentDateAfter(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null : cb.greaterThanOrEqualTo(root.get("appointmentDate"), date);
    }

    /**
     * 임용일자 이전 필터링
     */
    public static Specification<Professor> appointmentDateBefore(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null : cb.lessThanOrEqualTo(root.get("appointmentDate"), date);
    }

    /**
     * 교번 패턴으로 검색 (부분 일치)
     */
    public static Specification<Professor> professorIdLike(String pattern) {
        return (root, query, cb) ->
                pattern == null || pattern.isBlank() ? null :
                        cb.like(root.get("professorId").as(String.class), "%" + pattern + "%");
    }

    /**
     * 여러 교번으로 필터링
     */
    public static Specification<Professor> byProfessorIds(java.util.List<Long> professorIds) {
        return (root, query, cb) ->
                professorIds == null || professorIds.isEmpty() ? null : root.get("professorId").in(professorIds);
    }

    /**
     * 학과 ID로 필터링 (ProfessorDepartment 조인)
     */
    public static Specification<Professor> byDepartmentId(Long departmentId) {
        return (root, query, cb) ->
                departmentId == null ? null :
                        cb.equal(root.get("professorDepartment").get("department").get("id"), departmentId);
    }

    /**
     * 사용자 삭제되지 않은 교수만 조회
     */
    public static Specification<Professor> userNotDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("user").get("deletedAt"));
    }
}

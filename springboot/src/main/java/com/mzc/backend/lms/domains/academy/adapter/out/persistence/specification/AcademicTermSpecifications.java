package com.mzc.backend.lms.domains.academy.adapter.out.persistence.specification;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

/**
 * AcademicTerm 엔티티용 Specification 클래스
 */
public final class AcademicTermSpecifications {

    private AcademicTermSpecifications() {
    }

    public static Specification<AcademicTerm> byYear(Integer year) {
        return (root, query, cb) ->
                year == null ? null : cb.equal(root.get("year"), year);
    }

    public static Specification<AcademicTerm> byTermType(String termType) {
        return (root, query, cb) ->
                termType == null || termType.isBlank() ? null : cb.equal(root.get("termType"), termType);
    }

    public static Specification<AcademicTerm> currentTerm(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) {
                return null;
            }
            return cb.and(
                    cb.lessThanOrEqualTo(root.get("startDate"), date),
                    cb.greaterThanOrEqualTo(root.get("endDate"), date)
            );
        };
    }

    public static Specification<AcademicTerm> startDateAfter(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null : cb.greaterThanOrEqualTo(root.get("startDate"), date);
    }

    public static Specification<AcademicTerm> endDateBefore(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null : cb.lessThanOrEqualTo(root.get("endDate"), date);
    }
}

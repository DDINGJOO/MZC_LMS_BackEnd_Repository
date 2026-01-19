package com.mzc.backend.lms.domains.attendance.adapter.out.persistence.specification;

import com.mzc.backend.lms.domains.attendance.adapter.out.persistence.entity.WeekAttendance;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * WeekAttendance 엔티티용 Specification 클래스
 */
public final class WeekAttendanceSpecifications {

    private WeekAttendanceSpecifications() {
    }

    public static Specification<WeekAttendance> byStudentId(Long studentId) {
        return (root, query, cb) ->
                studentId == null ? null : cb.equal(root.get("studentId"), studentId);
    }

    public static Specification<WeekAttendance> byCourseId(Long courseId) {
        return (root, query, cb) ->
                courseId == null ? null : cb.equal(root.get("courseId"), courseId);
    }

    public static Specification<WeekAttendance> byWeekId(Long weekId) {
        return (root, query, cb) ->
                weekId == null ? null : cb.equal(root.get("weekId"), weekId);
    }

    public static Specification<WeekAttendance> byWeekIds(List<Long> weekIds) {
        return (root, query, cb) ->
                weekIds == null || weekIds.isEmpty() ? null : root.get("weekId").in(weekIds);
    }

    public static Specification<WeekAttendance> isCompleted(Boolean completed) {
        return (root, query, cb) ->
                completed == null ? null : cb.equal(root.get("isCompleted"), completed);
    }

    public static Specification<WeekAttendance> completed() {
        return (root, query, cb) -> cb.equal(root.get("isCompleted"), true);
    }

    public static Specification<WeekAttendance> incomplete() {
        return (root, query, cb) -> cb.equal(root.get("isCompleted"), false);
    }
}

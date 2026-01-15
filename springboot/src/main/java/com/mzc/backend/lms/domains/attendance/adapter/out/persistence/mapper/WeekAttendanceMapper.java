package com.mzc.backend.lms.domains.attendance.adapter.out.persistence.mapper;

import com.mzc.backend.lms.domains.attendance.adapter.out.persistence.entity.WeekAttendance;
import com.mzc.backend.lms.domains.attendance.domain.model.WeekAttendanceDomain;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * WeekAttendance Entity <-> Domain 변환 Mapper
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeekAttendanceMapper {

    /**
     * Entity -> Domain 변환
     */
    public static WeekAttendanceDomain toDomain(WeekAttendance entity) {
        if (entity == null) {
            return null;
        }
        return WeekAttendanceDomain.builder()
                .id(entity.getId())
                .studentId(entity.getStudentId())
                .weekId(entity.getWeekId())
                .courseId(entity.getCourseId())
                .isCompleted(entity.getIsCompleted())
                .completedVideoCount(entity.getCompletedVideoCount())
                .totalVideoCount(entity.getTotalVideoCount())
                .firstAccessedAt(entity.getFirstAccessedAt())
                .completedAt(entity.getCompletedAt())
                .build();
    }
}

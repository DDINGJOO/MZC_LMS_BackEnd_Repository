package com.mzc.backend.lms.domains.course.grade.adapter.out.external;

import com.mzc.backend.lms.domains.course.grade.application.port.out.AttendancePort;
import com.mzc.backend.lms.domains.attendance.repository.WeekAttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 출석 외부 Adapter (attendance 도메인)
 */
@Component
@RequiredArgsConstructor
public class AttendanceAdapter implements AttendancePort {

    private final WeekAttendanceRepository weekAttendanceRepository;

    @Override
    public int countCompletedByStudentAndCourse(Long studentId, Long courseId) {
        return weekAttendanceRepository.countCompletedByStudentAndCourse(studentId, courseId);
    }

    @Override
    public Map<Long, Integer> getAttendanceStatsByCourse(Long courseId) {
        Map<Long, Integer> result = new HashMap<>();
        List<Object[]> stats = weekAttendanceRepository.getAttendanceStatsByCourse(courseId);
        for (Object[] row : stats) {
            Long studentId = (Long) row[0];
            Long completedCount = (Long) row[2];
            result.put(studentId, completedCount != null ? completedCount.intValue() : 0);
        }
        return result;
    }
}

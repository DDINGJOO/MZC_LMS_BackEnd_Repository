package com.mzc.backend.lms.domains.course.grade.application.port.out;

import java.util.Map;

/**
 * 출석 외부 Port (attendance 도메인)
 */
public interface AttendancePort {

    /**
     * 학생 ID와 강의 ID로 출석 완료 주차 수 조회
     */
    int countCompletedByStudentAndCourse(Long studentId, Long courseId);

    /**
     * 강의 ID로 학생별 출석 통계 조회
     * @return Map<studentId, completedCount>
     */
    Map<Long, Integer> getAttendanceStatsByCourse(Long courseId);
}

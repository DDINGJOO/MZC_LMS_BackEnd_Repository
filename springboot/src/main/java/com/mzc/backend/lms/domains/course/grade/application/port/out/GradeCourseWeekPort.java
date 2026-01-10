package com.mzc.backend.lms.domains.course.grade.application.port.out;

/**
 * 강의 주차 외부 Port (course/course 도메인) - grade 전용
 */
public interface GradeCourseWeekPort {

    /**
     * 강의 ID로 주차 수 조회
     */
    long countByCourseId(Long courseId);
}

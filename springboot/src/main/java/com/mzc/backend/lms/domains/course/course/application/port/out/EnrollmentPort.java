package com.mzc.backend.lms.domains.course.course.application.port.out;

/**
 * 수강신청 외부 Port (enrollment 도메인)
 */
public interface EnrollmentPort {

    /**
     * 강의 수강생 수 조회
     */
    long countByCourseId(Long courseId);

    /**
     * 학생이 해당 강의를 수강 중인지 확인
     */
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}

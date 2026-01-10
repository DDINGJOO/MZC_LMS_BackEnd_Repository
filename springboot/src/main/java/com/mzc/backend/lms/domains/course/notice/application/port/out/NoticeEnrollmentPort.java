package com.mzc.backend.lms.domains.course.notice.application.port.out;

/**
 * 수강신청 외부 Port (enrollment 도메인) - notice 전용
 */
public interface NoticeEnrollmentPort {

    /**
     * 수강신청 여부 확인
     */
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}

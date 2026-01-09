package com.mzc.backend.lms.domains.enrollment.application.port.out;

/**
 * 수강신청 이벤트 발행을 위한 Port
 */
public interface EnrollmentEventPort {

    /**
     * 수강신청 완료 이벤트 발행
     */
    void publishEnrollmentCreated(Long studentId, Long courseId, String courseName, String sectionNumber);

    /**
     * 수강신청 취소 이벤트 발행
     */
    void publishEnrollmentCancelled(Long studentId, Long courseId, String courseName, String sectionNumber);
}

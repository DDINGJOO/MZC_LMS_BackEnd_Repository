package com.mzc.backend.lms.domains.course.notice.application.port.out;

import java.util.List;

/**
 * 수강신청 외부 Port (enrollment 도메인) - notice 전용
 */
public interface NoticeEnrollmentPort {

    /**
     * 수강신청 여부 확인
     */
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * 강의 수강생 ID 목록 조회
     */
    List<Long> findStudentIdsByCourseId(Long courseId);
}

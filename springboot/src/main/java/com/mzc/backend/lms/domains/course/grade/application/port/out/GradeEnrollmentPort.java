package com.mzc.backend.lms.domains.course.grade.application.port.out;

import java.util.List;

/**
 * 수강신청 외부 Port (enrollment 도메인) - grade 전용
 */
public interface GradeEnrollmentPort {

    /**
     * 강의 ID로 수강생 정보 조회
     */
    List<EnrolledStudentInfo> findStudentsByCourseId(Long courseId);

    /**
     * 강의 수강생 정보 DTO
     */
    record EnrolledStudentInfo(
            Long studentId,
            Long studentNumber
    ) {}
}

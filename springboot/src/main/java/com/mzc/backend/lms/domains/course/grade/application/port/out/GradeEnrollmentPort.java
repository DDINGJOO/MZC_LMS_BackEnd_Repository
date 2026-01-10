package com.mzc.backend.lms.domains.course.grade.application.port.out;

import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity.Enrollment;

import java.util.List;

/**
 * 수강신청 외부 Port (enrollment 도메인) - grade 전용
 */
public interface GradeEnrollmentPort {

    /**
     * 강의 ID로 수강신청 목록 조회 (학생 정보 포함)
     */
    List<Enrollment> findByCourseIdWithStudent(Long courseId);
}

package com.mzc.backend.lms.domains.course.grade.application.port.out;

import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.Course;

import java.util.List;
import java.util.Optional;

/**
 * 강의 외부 Port (course/course 도메인) - grade 전용
 */
public interface GradeCoursePort {

    /**
     * ID로 강의 조회
     */
    Optional<Course> findById(Long id);

    /**
     * 학기 ID로 강의 목록 조회
     */
    List<Course> findByAcademicTermId(Long academicTermId);

    /**
     * Course ID 목록으로 Subject 포함 조회
     */
    List<Course> findByIdInWithSubject(List<Long> ids);
}

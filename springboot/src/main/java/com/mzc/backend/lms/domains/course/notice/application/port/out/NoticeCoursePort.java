package com.mzc.backend.lms.domains.course.notice.application.port.out;

import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.Course;

import java.util.Optional;

/**
 * 강의 외부 Port (course/course 도메인) - notice 전용
 */
public interface NoticeCoursePort {

    /**
     * ID로 강의 조회
     */
    Optional<Course> findById(Long id);
}

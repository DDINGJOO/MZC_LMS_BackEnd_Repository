package com.mzc.backend.lms.domains.course.notice.adapter.out.external;

import com.mzc.backend.lms.domains.course.notice.application.port.out.NoticeCoursePort;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.Course;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 강의 외부 Adapter (course/course 도메인) - notice 전용
 */
@Component
@RequiredArgsConstructor
public class NoticeCourseAdapter implements NoticeCoursePort {

    private final CourseRepository courseRepository;

    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }
}

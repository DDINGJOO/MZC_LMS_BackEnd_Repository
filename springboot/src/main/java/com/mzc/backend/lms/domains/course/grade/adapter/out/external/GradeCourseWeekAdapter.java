package com.mzc.backend.lms.domains.course.grade.adapter.out.external;

import com.mzc.backend.lms.domains.course.grade.application.port.out.GradeCourseWeekPort;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.repository.CourseWeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 강의 주차 외부 Adapter (course/course 도메인) - grade 전용
 */
@Component
@RequiredArgsConstructor
public class GradeCourseWeekAdapter implements GradeCourseWeekPort {

    private final CourseWeekRepository courseWeekRepository;

    @Override
    public long countByCourseId(Long courseId) {
        return courseWeekRepository.countByCourseId(courseId);
    }
}

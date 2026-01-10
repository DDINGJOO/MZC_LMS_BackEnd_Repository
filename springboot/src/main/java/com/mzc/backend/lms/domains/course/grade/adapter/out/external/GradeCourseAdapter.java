package com.mzc.backend.lms.domains.course.grade.adapter.out.external;

import com.mzc.backend.lms.domains.course.grade.application.port.out.GradeCoursePort;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.Course;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 강의 외부 Adapter (course/course 도메인) - grade 전용
 */
@Component
@RequiredArgsConstructor
public class GradeCourseAdapter implements GradeCoursePort {

    private final CourseRepository courseRepository;

    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public List<Course> findByAcademicTermId(Long academicTermId) {
        return courseRepository.findByAcademicTermId(academicTermId);
    }

    @Override
    public List<Course> findByIdInWithSubject(List<Long> ids) {
        return courseRepository.findByIdInWithSubject(ids);
    }
}

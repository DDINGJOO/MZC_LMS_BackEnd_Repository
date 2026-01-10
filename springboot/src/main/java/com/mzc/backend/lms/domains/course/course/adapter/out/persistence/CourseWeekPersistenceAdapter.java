package com.mzc.backend.lms.domains.course.course.adapter.out.persistence;

import com.mzc.backend.lms.domains.course.course.application.port.out.CourseWeekRepositoryPort;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.CourseWeek;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.repository.CourseWeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 강의 주차 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class CourseWeekPersistenceAdapter implements CourseWeekRepositoryPort {

    private final CourseWeekRepository courseWeekRepository;

    @Override
    public CourseWeek save(CourseWeek week) {
        return courseWeekRepository.save(week);
    }

    @Override
    public Optional<CourseWeek> findById(Long id) {
        return courseWeekRepository.findById(id);
    }

    @Override
    public List<CourseWeek> findByCourseId(Long courseId) {
        return courseWeekRepository.findByCourseId(courseId);
    }

    @Override
    public void delete(CourseWeek week) {
        courseWeekRepository.delete(week);
    }

    @Override
    public boolean existsByCourseIdAndWeekNumber(Long courseId, Integer weekNumber) {
        return courseWeekRepository.existsByCourseIdAndWeekNumber(courseId, weekNumber);
    }

    @Override
    public long countByCourseId(Long courseId) {
        return courseWeekRepository.countByCourseId(courseId);
    }
}

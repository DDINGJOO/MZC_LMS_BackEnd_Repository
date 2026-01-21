package com.mzc.lms.course.application.service;

import com.mzc.lms.course.application.port.in.CourseUseCase;
import com.mzc.lms.course.application.port.out.CourseRepositoryPort;
import com.mzc.lms.course.application.port.out.EventPublisherPort;
import com.mzc.lms.course.domain.event.CourseEvent;
import com.mzc.lms.course.domain.model.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService implements CourseUseCase {

    private static final String CACHE_COURSES = "courses";
    private static final String CACHE_COURSE_LIST = "courseList";

    private final CourseRepositoryPort courseRepository;
    private final EventPublisherPort eventPublisher;

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_COURSES, CACHE_COURSE_LIST}, allEntries = true)
    public Course createCourse(CreateCourseCommand command) {
        if (courseRepository.existsBySubjectIdAndAcademicTermIdAndSectionNumber(
                command.subjectId(), command.academicTermId(), command.sectionNumber())) {
            throw new IllegalArgumentException("Course section already exists");
        }

        Course course = Course.create(
                command.subjectId(),
                command.professorId(),
                command.academicTermId(),
                command.sectionNumber(),
                command.maxStudents(),
                command.description()
        );

        Course saved = courseRepository.save(course);
        eventPublisher.publish(CourseEvent.courseCreated(saved.getId(), saved));

        return saved;
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_COURSES, CACHE_COURSE_LIST}, allEntries = true)
    public Course updateCourse(Long id, UpdateCourseCommand command) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + id));

        Course updated = existing.update(
                command.professorId(),
                command.maxStudents(),
                command.description()
        );

        Course saved = courseRepository.save(updated);
        eventPublisher.publish(CourseEvent.courseUpdated(saved.getId(), saved));

        return saved;
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_COURSES, CACHE_COURSE_LIST}, allEntries = true)
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new IllegalArgumentException("Course not found: " + id);
        }
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_COURSES, CACHE_COURSE_LIST}, allEntries = true)
    public Course deactivateCourse(Long id) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + id));

        Course deactivated = existing.deactivate();
        Course saved = courseRepository.save(deactivated);
        eventPublisher.publish(CourseEvent.courseDeactivated(saved.getId()));

        return saved;
    }

    @Override
    @Cacheable(value = CACHE_COURSES, key = "#id")
    public Optional<Course> getCourse(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    @Cacheable(value = CACHE_COURSE_LIST, key = "'filter:' + #criteria.hashCode() + ':' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<Course> getCourses(CourseSearchCriteria criteria, Pageable pageable) {
        return courseRepository.findWithFilters(
                criteria.subjectId(),
                criteria.professorId(),
                criteria.academicTermId(),
                criteria.isActive(),
                criteria.hasAvailableSeats(),
                pageable
        );
    }

    @Override
    @Cacheable(value = CACHE_COURSE_LIST, key = "'subject:' + #subjectId")
    public List<Course> getCoursesBySubject(Long subjectId) {
        return courseRepository.findBySubjectId(subjectId);
    }

    @Override
    @Cacheable(value = CACHE_COURSE_LIST, key = "'professor:' + #professorId")
    public List<Course> getCoursesByProfessor(Long professorId) {
        return courseRepository.findByProfessorId(professorId);
    }

    @Override
    @Cacheable(value = CACHE_COURSE_LIST, key = "'term:' + #academicTermId")
    public List<Course> getCoursesByAcademicTerm(Long academicTermId) {
        return courseRepository.findByAcademicTermId(academicTermId);
    }

    @Override
    @Cacheable(value = CACHE_COURSE_LIST, key = "'subject:' + #subjectId + ':term:' + #academicTermId")
    public List<Course> getCoursesBySubjectAndTerm(Long subjectId, Long academicTermId) {
        return courseRepository.findBySubjectIdAndAcademicTermId(subjectId, academicTermId);
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_COURSES, CACHE_COURSE_LIST}, allEntries = true)
    public Course incrementStudentCount(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        Course updated = course.incrementStudents();
        return courseRepository.save(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_COURSES, CACHE_COURSE_LIST}, allEntries = true)
    public Course decrementStudentCount(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        Course updated = course.decrementStudents();
        return courseRepository.save(updated);
    }
}

package com.mzc.lms.course.application.port.in;

import com.mzc.lms.course.domain.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CourseUseCase {

    Course createCourse(CreateCourseCommand command);

    Course updateCourse(Long id, UpdateCourseCommand command);

    void deleteCourse(Long id);

    Course deactivateCourse(Long id);

    Optional<Course> getCourse(Long id);

    Page<Course> getCourses(CourseSearchCriteria criteria, Pageable pageable);

    List<Course> getCoursesBySubject(Long subjectId);

    List<Course> getCoursesByProfessor(Long professorId);

    List<Course> getCoursesByAcademicTerm(Long academicTermId);

    List<Course> getCoursesBySubjectAndTerm(Long subjectId, Long academicTermId);

    Course incrementStudentCount(Long courseId);

    Course decrementStudentCount(Long courseId);

    record CreateCourseCommand(
            Long subjectId,
            Long professorId,
            Long academicTermId,
            String sectionNumber,
            Integer maxStudents,
            String description
    ) {}

    record UpdateCourseCommand(
            Long professorId,
            Integer maxStudents,
            String description
    ) {}

    record CourseSearchCriteria(
            Long subjectId,
            Long professorId,
            Long academicTermId,
            Boolean isActive,
            Boolean hasAvailableSeats
    ) {}
}

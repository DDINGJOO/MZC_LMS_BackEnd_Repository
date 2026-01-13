package com.mzc.backend.lms.domains.course.course.adapter.out.persistence;

import com.mzc.backend.lms.domains.course.course.application.port.out.CourseRepositoryPort;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.Course;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.repository.CourseRepository;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * 강의 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class CoursePersistenceAdapter implements CourseRepositoryPort {

    private final CourseRepository courseRepository;

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public Optional<Course> findByIdWithLock(Long id) {
        return courseRepository.findByIdWithLock(id);
    }

    @Override
    public void delete(Course course) {
        courseRepository.delete(course);
    }

    @Override
    public List<Course> findByAcademicTermId(Long academicTermId) {
        return courseRepository.findByAcademicTermId(academicTermId);
    }

    @Override
    public List<Course> findBySubjectDepartmentId(Long departmentId) {
        return courseRepository.findBySubjectDepartmentId(departmentId);
    }

    @Override
    public List<Course> findByProfessorProfessorId(Long professorId) {
        return courseRepository.findByProfessorProfessorId(professorId);
    }

    @Override
    public List<Course> findByProfessorProfessorIdAndAcademicTermId(Long professorId, Long academicTermId) {
        return courseRepository.findByProfessorProfessorIdAndAcademicTermId(professorId, academicTermId);
    }

    @Override
    public List<AcademicTerm> findDistinctAcademicTermsByProfessorId(Long professorId) {
        return courseRepository.findDistinctAcademicTermsByProfessorId(professorId);
    }

    @Override
    public List<Course> findByIdInWithSubject(List<Long> ids) {
        return courseRepository.findByIdInWithSubject(ids);
    }

    @Override
    public boolean existsBySubjectIdAndAcademicTermIdAndSectionNumber(Long subjectId, Long academicTermId, String sectionNumber) {
        return courseRepository.existsBySubjectIdAndAcademicTermIdAndSectionNumber(subjectId, academicTermId, sectionNumber);
    }

    @Override
    public boolean existsByProfessorAndTimeConflict(Long professorId, Long academicTermId,
                                                     DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        return courseRepository.existsByProfessorAndTimeConflict(professorId, academicTermId, dayOfWeek, startTime, endTime);
    }

    @Override
    public boolean existsByProfessorAndTimeConflictExcludingCourse(Long professorId, Long academicTermId,
                                                                    Long excludeCourseId, DayOfWeek dayOfWeek,
                                                                    LocalTime startTime, LocalTime endTime) {
        return courseRepository.existsByProfessorAndTimeConflictExcludingCourse(
                professorId, academicTermId, excludeCourseId, dayOfWeek, startTime, endTime);
    }

    @Override
    public long countBySubjectIdAndAcademicTermId(Long subjectId, Long academicTermId) {
        return courseRepository.countBySubjectIdAndAcademicTermId(subjectId, academicTermId);
    }
}

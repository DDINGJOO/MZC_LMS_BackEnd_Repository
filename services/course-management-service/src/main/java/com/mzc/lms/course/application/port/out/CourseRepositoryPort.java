package com.mzc.lms.course.application.port.out;

import com.mzc.lms.course.domain.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CourseRepositoryPort {

    Course save(Course course);

    Optional<Course> findById(Long id);

    Page<Course> findAll(Pageable pageable);

    Page<Course> findWithFilters(
            Long subjectId,
            Long professorId,
            Long academicTermId,
            Boolean isActive,
            Boolean hasAvailableSeats,
            Pageable pageable
    );

    List<Course> findBySubjectId(Long subjectId);

    List<Course> findByProfessorId(Long professorId);

    List<Course> findByAcademicTermId(Long academicTermId);

    List<Course> findBySubjectIdAndAcademicTermId(Long subjectId, Long academicTermId);

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsBySubjectIdAndAcademicTermIdAndSectionNumber(
            Long subjectId, Long academicTermId, String sectionNumber);
}

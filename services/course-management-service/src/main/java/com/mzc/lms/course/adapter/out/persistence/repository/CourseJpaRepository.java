package com.mzc.lms.course.adapter.out.persistence.repository;

import com.mzc.lms.course.adapter.out.persistence.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CourseJpaRepository extends JpaRepository<CourseEntity, Long>, JpaSpecificationExecutor<CourseEntity> {

    List<CourseEntity> findBySubjectId(Long subjectId);

    List<CourseEntity> findByProfessorId(Long professorId);

    List<CourseEntity> findByAcademicTermId(Long academicTermId);

    List<CourseEntity> findBySubjectIdAndAcademicTermId(Long subjectId, Long academicTermId);

    boolean existsBySubjectIdAndAcademicTermIdAndSectionNumber(
            Long subjectId, Long academicTermId, String sectionNumber);
}

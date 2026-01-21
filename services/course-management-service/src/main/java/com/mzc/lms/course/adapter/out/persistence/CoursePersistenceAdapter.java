package com.mzc.lms.course.adapter.out.persistence;

import com.mzc.lms.course.adapter.out.persistence.entity.CourseEntity;
import com.mzc.lms.course.adapter.out.persistence.repository.CourseJpaRepository;
import com.mzc.lms.course.application.port.out.CourseRepositoryPort;
import com.mzc.lms.course.domain.model.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CoursePersistenceAdapter implements CourseRepositoryPort {

    private final CourseJpaRepository courseJpaRepository;

    @Override
    public Course save(Course course) {
        CourseEntity entity = CourseEntity.fromDomain(course);
        CourseEntity saved = courseJpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courseJpaRepository.findById(id)
                .map(CourseEntity::toDomain);
    }

    @Override
    public Page<Course> findAll(Pageable pageable) {
        return courseJpaRepository.findAll(pageable)
                .map(CourseEntity::toDomain);
    }

    @Override
    public Page<Course> findWithFilters(
            Long subjectId,
            Long professorId,
            Long academicTermId,
            Boolean isActive,
            Boolean hasAvailableSeats,
            Pageable pageable
    ) {
        Specification<CourseEntity> spec = Specification.where(null);

        if (subjectId != null) {
            spec = spec.and(CourseSpecification.hasSubjectId(subjectId));
        }
        if (professorId != null) {
            spec = spec.and(CourseSpecification.hasProfessorId(professorId));
        }
        if (academicTermId != null) {
            spec = spec.and(CourseSpecification.hasAcademicTermId(academicTermId));
        }
        if (isActive != null) {
            spec = spec.and(CourseSpecification.isActive(isActive));
        }
        if (hasAvailableSeats != null && hasAvailableSeats) {
            spec = spec.and(CourseSpecification.hasAvailableSeats(hasAvailableSeats));
        }

        return courseJpaRepository.findAll(spec, pageable)
                .map(CourseEntity::toDomain);
    }

    @Override
    public List<Course> findBySubjectId(Long subjectId) {
        return courseJpaRepository.findBySubjectId(subjectId).stream()
                .map(CourseEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> findByProfessorId(Long professorId) {
        return courseJpaRepository.findByProfessorId(professorId).stream()
                .map(CourseEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> findByAcademicTermId(Long academicTermId) {
        return courseJpaRepository.findByAcademicTermId(academicTermId).stream()
                .map(CourseEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> findBySubjectIdAndAcademicTermId(Long subjectId, Long academicTermId) {
        return courseJpaRepository.findBySubjectIdAndAcademicTermId(subjectId, academicTermId).stream()
                .map(CourseEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        courseJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return courseJpaRepository.existsById(id);
    }

    @Override
    public boolean existsBySubjectIdAndAcademicTermIdAndSectionNumber(
            Long subjectId, Long academicTermId, String sectionNumber) {
        return courseJpaRepository.existsBySubjectIdAndAcademicTermIdAndSectionNumber(
                subjectId, academicTermId, sectionNumber);
    }
}

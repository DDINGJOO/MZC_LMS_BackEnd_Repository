package com.mzc.lms.catalog.application.port.out;

import com.mzc.lms.catalog.domain.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SubjectRepositoryPort {

    Subject save(Subject subject);

    Optional<Subject> findById(Long id);

    Optional<Subject> findBySubjectCode(String subjectCode);

    Page<Subject> findAll(Pageable pageable);

    Page<Subject> findWithFilters(
            String keyword,
            Long departmentId,
            Long courseTypeId,
            Integer credits,
            Boolean isActive,
            Pageable pageable
    );

    Page<Subject> search(String query, Pageable pageable);

    Page<Subject> findByDepartmentId(Long departmentId, Pageable pageable);

    Page<Subject> findByCourseTypeId(Long courseTypeId, Pageable pageable);

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsBySubjectCode(String subjectCode);
}

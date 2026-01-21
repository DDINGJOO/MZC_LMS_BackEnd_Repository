package com.mzc.lms.catalog.adapter.out.persistence;

import com.mzc.lms.catalog.adapter.out.persistence.entity.SubjectEntity;
import com.mzc.lms.catalog.adapter.out.persistence.repository.SubjectJpaRepository;
import com.mzc.lms.catalog.application.port.out.SubjectRepositoryPort;
import com.mzc.lms.catalog.domain.model.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SubjectPersistenceAdapter implements SubjectRepositoryPort {

    private final SubjectJpaRepository subjectJpaRepository;

    @Override
    public Subject save(Subject subject) {
        SubjectEntity entity = SubjectEntity.fromDomain(subject);
        SubjectEntity saved = subjectJpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Subject> findById(Long id) {
        return subjectJpaRepository.findById(id)
                .map(SubjectEntity::toDomain);
    }

    @Override
    public Optional<Subject> findBySubjectCode(String subjectCode) {
        return subjectJpaRepository.findBySubjectCode(subjectCode)
                .map(SubjectEntity::toDomain);
    }

    @Override
    public Page<Subject> findAll(Pageable pageable) {
        return subjectJpaRepository.findAll(pageable)
                .map(SubjectEntity::toDomain);
    }

    @Override
    public Page<Subject> findWithFilters(
            String keyword,
            Long departmentId,
            Long courseTypeId,
            Integer credits,
            Boolean isActive,
            Pageable pageable
    ) {
        Specification<SubjectEntity> spec = Specification.where(null);

        if (keyword != null && !keyword.trim().isEmpty()) {
            spec = spec.and(SubjectSpecification.hasKeyword(keyword));
        }
        if (departmentId != null) {
            spec = spec.and(SubjectSpecification.hasDepartmentId(departmentId));
        }
        if (courseTypeId != null) {
            spec = spec.and(SubjectSpecification.hasCourseTypeId(courseTypeId));
        }
        if (credits != null) {
            spec = spec.and(SubjectSpecification.hasCredits(credits));
        }
        if (isActive != null) {
            spec = spec.and(SubjectSpecification.isActive(isActive));
        }

        return subjectJpaRepository.findAll(spec, pageable)
                .map(SubjectEntity::toDomain);
    }

    @Override
    public Page<Subject> search(String query, Pageable pageable) {
        return subjectJpaRepository.search(query, pageable)
                .map(SubjectEntity::toDomain);
    }

    @Override
    public Page<Subject> findByDepartmentId(Long departmentId, Pageable pageable) {
        return subjectJpaRepository.findByDepartmentId(departmentId, pageable)
                .map(SubjectEntity::toDomain);
    }

    @Override
    public Page<Subject> findByCourseTypeId(Long courseTypeId, Pageable pageable) {
        return subjectJpaRepository.findByCourseTypeId(courseTypeId, pageable)
                .map(SubjectEntity::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        subjectJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return subjectJpaRepository.existsById(id);
    }

    @Override
    public boolean existsBySubjectCode(String subjectCode) {
        return subjectJpaRepository.existsBySubjectCode(subjectCode);
    }
}

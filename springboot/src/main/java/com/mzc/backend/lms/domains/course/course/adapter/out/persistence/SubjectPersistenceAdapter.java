package com.mzc.backend.lms.domains.course.course.adapter.out.persistence;

import com.mzc.backend.lms.domains.course.course.application.port.out.SubjectRepositoryPort;
import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.entity.Subject;
import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Subject 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class SubjectPersistenceAdapter implements SubjectRepositoryPort {

    private final SubjectRepository subjectRepository;

    @Override
    public Subject save(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Override
    public Optional<Subject> findById(Long id) {
        return subjectRepository.findById(id);
    }

    @Override
    public Optional<Subject> findByIdWithDetails(Long id) {
        return subjectRepository.findByIdWithDetails(id);
    }

    @Override
    public boolean existsByDepartmentIdAndSubjectCode(Long departmentId, String subjectCode) {
        return subjectRepository.existsByDepartmentIdAndSubjectCode(departmentId, subjectCode);
    }

    @Override
    public Page<Subject> findSubjectsWithFilters(Long departmentId, String keyword, Long courseTypeId, Integer credits, Pageable pageable) {
        return subjectRepository.findSubjectsWithFilters(departmentId, keyword, courseTypeId, credits, pageable);
    }

    @Override
    public Page<Subject> searchSubjects(String query, Pageable pageable) {
        return subjectRepository.searchSubjects(query, pageable);
    }
}

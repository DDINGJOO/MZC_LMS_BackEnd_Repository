package com.mzc.backend.lms.domains.course.subject.adapter.out.persistence;

import com.mzc.backend.lms.domains.course.subject.application.port.out.SubjectRepositoryPort;
import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.entity.Subject;
import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 과목 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class SubjectPersistenceAdapter implements SubjectRepositoryPort {

    private final SubjectRepository subjectRepository;

    @Override
    public Optional<Subject> findByIdWithDetails(Long subjectId) {
        return subjectRepository.findByIdWithDetails(subjectId);
    }

    @Override
    public Page<Subject> findSubjectsWithFilters(
            Long departmentId,
            String keyword,
            Long courseTypeId,
            Integer credits,
            Pageable pageable
    ) {
        return subjectRepository.findSubjectsWithFilters(departmentId, keyword, courseTypeId, credits, pageable);
    }

    @Override
    public Page<Subject> searchSubjects(String query, Pageable pageable) {
        return subjectRepository.searchSubjects(query, pageable);
    }
}

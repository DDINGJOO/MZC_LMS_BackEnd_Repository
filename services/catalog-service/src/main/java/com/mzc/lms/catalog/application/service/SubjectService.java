package com.mzc.lms.catalog.application.service;

import com.mzc.lms.catalog.application.port.in.SubjectUseCase;
import com.mzc.lms.catalog.application.port.out.EventPublisherPort;
import com.mzc.lms.catalog.application.port.out.SubjectPrerequisiteRepositoryPort;
import com.mzc.lms.catalog.application.port.out.SubjectRepositoryPort;
import com.mzc.lms.catalog.domain.event.CatalogEvent;
import com.mzc.lms.catalog.domain.model.Subject;
import com.mzc.lms.catalog.domain.model.SubjectPrerequisite;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectService implements SubjectUseCase {

    private static final String CACHE_SUBJECTS = "subjects";
    private static final String CACHE_SUBJECT_LIST = "subjectList";

    private final SubjectRepositoryPort subjectRepository;
    private final SubjectPrerequisiteRepositoryPort prerequisiteRepository;
    private final EventPublisherPort eventPublisher;

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_SUBJECTS, CACHE_SUBJECT_LIST}, allEntries = true)
    public Subject createSubject(CreateSubjectCommand command) {
        if (subjectRepository.existsBySubjectCode(command.subjectCode())) {
            throw new IllegalArgumentException("Subject code already exists: " + command.subjectCode());
        }

        Subject subject = Subject.create(
                command.subjectCode(),
                command.subjectName(),
                command.subjectDescription(),
                command.departmentId(),
                command.courseTypeId(),
                command.credits(),
                command.theoryHours(),
                command.practiceHours(),
                command.description()
        );

        Subject saved = subjectRepository.save(subject);
        eventPublisher.publish(CatalogEvent.subjectCreated(saved.getId(), saved));

        return saved;
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_SUBJECTS, CACHE_SUBJECT_LIST}, allEntries = true)
    public Subject updateSubject(Long id, UpdateSubjectCommand command) {
        Subject existing = subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found: " + id));

        Subject updated = existing.update(
                command.subjectName(),
                command.subjectDescription(),
                command.courseTypeId(),
                command.credits(),
                command.theoryHours(),
                command.practiceHours(),
                command.description()
        );

        Subject saved = subjectRepository.save(updated);
        eventPublisher.publish(CatalogEvent.subjectUpdated(saved.getId(), saved));

        return saved;
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_SUBJECTS, CACHE_SUBJECT_LIST}, allEntries = true)
    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new IllegalArgumentException("Subject not found: " + id);
        }
        subjectRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_SUBJECTS, CACHE_SUBJECT_LIST}, allEntries = true)
    public Subject deactivateSubject(Long id) {
        Subject existing = subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found: " + id));

        Subject deactivated = existing.deactivate();
        Subject saved = subjectRepository.save(deactivated);
        eventPublisher.publish(CatalogEvent.subjectDeactivated(saved.getId()));

        return saved;
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_SUBJECTS, CACHE_SUBJECT_LIST}, allEntries = true)
    public Subject activateSubject(Long id) {
        Subject existing = subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found: " + id));

        Subject activated = existing.activate();
        Subject saved = subjectRepository.save(activated);
        eventPublisher.publish(CatalogEvent.subjectActivated(saved.getId()));

        return saved;
    }

    @Override
    @Cacheable(value = CACHE_SUBJECTS, key = "#id")
    public Optional<Subject> getSubject(Long id) {
        return subjectRepository.findById(id);
    }

    @Override
    @Cacheable(value = CACHE_SUBJECTS, key = "'code:' + #subjectCode")
    public Optional<Subject> getSubjectByCode(String subjectCode) {
        return subjectRepository.findBySubjectCode(subjectCode);
    }

    @Override
    @Cacheable(value = CACHE_SUBJECT_LIST, key = "'filter:' + #criteria.hashCode() + ':' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<Subject> getSubjects(SubjectSearchCriteria criteria, Pageable pageable) {
        return subjectRepository.findWithFilters(
                criteria.keyword(),
                criteria.departmentId(),
                criteria.courseTypeId(),
                criteria.credits(),
                criteria.isActive(),
                pageable
        );
    }

    @Override
    public Page<Subject> searchSubjects(String query, Pageable pageable) {
        if (query == null || query.trim().length() < 2) {
            throw new IllegalArgumentException("Search query must be at least 2 characters");
        }
        return subjectRepository.search(query.trim(), pageable);
    }

    @Override
    @Cacheable(value = CACHE_SUBJECT_LIST, key = "'dept:' + #departmentId + ':' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<Subject> getSubjectsByDepartment(Long departmentId, Pageable pageable) {
        return subjectRepository.findByDepartmentId(departmentId, pageable);
    }

    @Override
    @Cacheable(value = CACHE_SUBJECT_LIST, key = "'type:' + #courseTypeId + ':' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<Subject> getSubjectsByCourseType(Long courseTypeId, Pageable pageable) {
        return subjectRepository.findByCourseTypeId(courseTypeId, pageable);
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_SUBJECTS, CACHE_SUBJECT_LIST}, allEntries = true)
    public void addPrerequisite(Long subjectId, Long prerequisiteId, Boolean isMandatory) {
        if (!subjectRepository.existsById(subjectId)) {
            throw new IllegalArgumentException("Subject not found: " + subjectId);
        }
        if (!subjectRepository.existsById(prerequisiteId)) {
            throw new IllegalArgumentException("Prerequisite subject not found: " + prerequisiteId);
        }
        if (subjectId.equals(prerequisiteId)) {
            throw new IllegalArgumentException("Subject cannot be its own prerequisite");
        }
        if (prerequisiteRepository.existsBySubjectIdAndPrerequisiteId(subjectId, prerequisiteId)) {
            throw new IllegalArgumentException("Prerequisite already exists");
        }

        SubjectPrerequisite prerequisite = SubjectPrerequisite.create(subjectId, prerequisiteId, isMandatory);
        prerequisiteRepository.save(prerequisite);
        eventPublisher.publish(CatalogEvent.prerequisiteAdded(subjectId, prerequisiteId));
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_SUBJECTS, CACHE_SUBJECT_LIST}, allEntries = true)
    public void removePrerequisite(Long subjectId, Long prerequisiteId) {
        if (!prerequisiteRepository.existsBySubjectIdAndPrerequisiteId(subjectId, prerequisiteId)) {
            throw new IllegalArgumentException("Prerequisite not found");
        }

        prerequisiteRepository.deleteBySubjectIdAndPrerequisiteId(subjectId, prerequisiteId);
        eventPublisher.publish(CatalogEvent.prerequisiteRemoved(subjectId, prerequisiteId));
    }
}

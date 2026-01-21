package com.mzc.lms.catalog.application.port.in;

import com.mzc.lms.catalog.domain.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SubjectUseCase {

    Subject createSubject(CreateSubjectCommand command);

    Subject updateSubject(Long id, UpdateSubjectCommand command);

    void deleteSubject(Long id);

    Subject deactivateSubject(Long id);

    Subject activateSubject(Long id);

    Optional<Subject> getSubject(Long id);

    Optional<Subject> getSubjectByCode(String subjectCode);

    Page<Subject> getSubjects(SubjectSearchCriteria criteria, Pageable pageable);

    Page<Subject> searchSubjects(String query, Pageable pageable);

    Page<Subject> getSubjectsByDepartment(Long departmentId, Pageable pageable);

    Page<Subject> getSubjectsByCourseType(Long courseTypeId, Pageable pageable);

    void addPrerequisite(Long subjectId, Long prerequisiteId, Boolean isMandatory);

    void removePrerequisite(Long subjectId, Long prerequisiteId);

    record CreateSubjectCommand(
            String subjectCode,
            String subjectName,
            String subjectDescription,
            Long departmentId,
            Long courseTypeId,
            Integer credits,
            Integer theoryHours,
            Integer practiceHours,
            String description
    ) {}

    record UpdateSubjectCommand(
            String subjectName,
            String subjectDescription,
            Long courseTypeId,
            Integer credits,
            Integer theoryHours,
            Integer practiceHours,
            String description
    ) {}

    record SubjectSearchCriteria(
            String keyword,
            Long departmentId,
            Long courseTypeId,
            Integer credits,
            Boolean isActive
    ) {}
}

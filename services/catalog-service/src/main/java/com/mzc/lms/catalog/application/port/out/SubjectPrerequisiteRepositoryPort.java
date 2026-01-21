package com.mzc.lms.catalog.application.port.out;

import com.mzc.lms.catalog.domain.model.SubjectPrerequisite;

import java.util.List;
import java.util.Optional;

public interface SubjectPrerequisiteRepositoryPort {

    SubjectPrerequisite save(SubjectPrerequisite prerequisite);

    Optional<SubjectPrerequisite> findById(Long id);

    List<SubjectPrerequisite> findBySubjectId(Long subjectId);

    List<SubjectPrerequisite> findByPrerequisiteId(Long prerequisiteId);

    Optional<SubjectPrerequisite> findBySubjectIdAndPrerequisiteId(Long subjectId, Long prerequisiteId);

    void deleteById(Long id);

    void deleteBySubjectIdAndPrerequisiteId(Long subjectId, Long prerequisiteId);

    boolean existsBySubjectIdAndPrerequisiteId(Long subjectId, Long prerequisiteId);
}

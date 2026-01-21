package com.mzc.lms.catalog.adapter.out.persistence.repository;

import com.mzc.lms.catalog.adapter.out.persistence.entity.SubjectPrerequisiteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectPrerequisiteJpaRepository extends JpaRepository<SubjectPrerequisiteEntity, Long> {

    List<SubjectPrerequisiteEntity> findBySubjectId(Long subjectId);

    List<SubjectPrerequisiteEntity> findByPrerequisiteId(Long prerequisiteId);

    Optional<SubjectPrerequisiteEntity> findBySubjectIdAndPrerequisiteId(Long subjectId, Long prerequisiteId);

    void deleteBySubjectIdAndPrerequisiteId(Long subjectId, Long prerequisiteId);

    boolean existsBySubjectIdAndPrerequisiteId(Long subjectId, Long prerequisiteId);
}

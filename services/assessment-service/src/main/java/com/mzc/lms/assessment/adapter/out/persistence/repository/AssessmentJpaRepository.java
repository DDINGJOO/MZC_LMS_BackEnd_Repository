package com.mzc.lms.assessment.adapter.out.persistence.repository;

import com.mzc.lms.assessment.adapter.out.persistence.entity.AssessmentEntity;
import com.mzc.lms.assessment.domain.model.AssessmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentJpaRepository extends JpaRepository<AssessmentEntity, Long> {

    List<AssessmentEntity> findByCourseId(Long courseId);

    List<AssessmentEntity> findByCourseIdAndType(Long courseId, AssessmentType type);

    List<AssessmentEntity> findByCourseIdAndIsPublishedTrue(Long courseId);
}

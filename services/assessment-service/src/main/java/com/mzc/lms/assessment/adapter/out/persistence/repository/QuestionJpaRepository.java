package com.mzc.lms.assessment.adapter.out.persistence.repository;

import com.mzc.lms.assessment.adapter.out.persistence.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long> {

    List<QuestionEntity> findByAssessmentIdOrderByOrderIndexAsc(Long assessmentId);

    void deleteByAssessmentId(Long assessmentId);
}

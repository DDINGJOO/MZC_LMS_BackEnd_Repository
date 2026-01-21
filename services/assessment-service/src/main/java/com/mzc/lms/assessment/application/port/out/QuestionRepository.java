package com.mzc.lms.assessment.application.port.out;

import com.mzc.lms.assessment.domain.model.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    Question save(Question question);

    Optional<Question> findById(Long id);

    List<Question> findByAssessmentId(Long assessmentId);

    List<Question> findByAssessmentIdOrderByOrderIndex(Long assessmentId);

    void deleteById(Long id);

    void deleteByAssessmentId(Long assessmentId);
}

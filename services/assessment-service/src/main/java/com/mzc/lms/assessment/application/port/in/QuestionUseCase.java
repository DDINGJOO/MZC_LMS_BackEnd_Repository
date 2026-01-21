package com.mzc.lms.assessment.application.port.in;

import com.mzc.lms.assessment.domain.model.Question;
import com.mzc.lms.assessment.domain.model.QuestionType;

import java.util.List;
import java.util.Optional;

public interface QuestionUseCase {

    Question createQuestion(Long assessmentId, QuestionType type, String questionText,
                            List<String> options, String correctAnswer, Integer points, Integer orderIndex);

    Question updateQuestion(Long id, String questionText, List<String> options,
                            String correctAnswer, Integer points, String explanation);

    Optional<Question> findById(Long id);

    List<Question> findByAssessmentId(Long assessmentId);

    void deleteQuestion(Long id);

    void reorderQuestions(Long assessmentId, List<Long> questionIds);
}

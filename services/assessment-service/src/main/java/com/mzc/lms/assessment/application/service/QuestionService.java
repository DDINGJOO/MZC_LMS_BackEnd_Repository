package com.mzc.lms.assessment.application.service;

import com.mzc.lms.assessment.application.port.in.QuestionUseCase;
import com.mzc.lms.assessment.application.port.out.QuestionRepository;
import com.mzc.lms.assessment.domain.model.Question;
import com.mzc.lms.assessment.domain.model.QuestionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService implements QuestionUseCase {

    private final QuestionRepository questionRepository;

    @Override
    @CacheEvict(value = "questions-by-assessment", key = "#assessmentId")
    public Question createQuestion(Long assessmentId, QuestionType type, String questionText,
                                    List<String> options, String correctAnswer, Integer points, Integer orderIndex) {
        log.info("Creating question for assessment: {}, type: {}", assessmentId, type);

        Question question = Question.create(assessmentId, type, questionText, options, correctAnswer, points, orderIndex);
        Question saved = questionRepository.save(question);

        log.info("Question created with id: {}", saved.getId());
        return saved;
    }

    @Override
    @CacheEvict(value = "questions-by-assessment", allEntries = true)
    public Question updateQuestion(Long id, String questionText, List<String> options,
                                    String correctAnswer, Integer points, String explanation) {
        log.info("Updating question: {}", id);

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found: " + id));

        Question updated = question.update(questionText, options, correctAnswer, points, explanation);
        return questionRepository.save(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Question> findById(Long id) {
        return questionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "questions-by-assessment", key = "#assessmentId")
    public List<Question> findByAssessmentId(Long assessmentId) {
        return questionRepository.findByAssessmentIdOrderByOrderIndex(assessmentId);
    }

    @Override
    @CacheEvict(value = "questions-by-assessment", allEntries = true)
    public void deleteQuestion(Long id) {
        log.info("Deleting question: {}", id);
        questionRepository.deleteById(id);
    }

    @Override
    @CacheEvict(value = "questions-by-assessment", key = "#assessmentId")
    public void reorderQuestions(Long assessmentId, List<Long> questionIds) {
        log.info("Reordering questions for assessment: {}", assessmentId);

        List<Question> questions = questionRepository.findByAssessmentId(assessmentId);
        for (int i = 0; i < questionIds.size(); i++) {
            Long questionId = questionIds.get(i);
            int finalI = i;
            questions.stream()
                    .filter(q -> q.getId().equals(questionId))
                    .findFirst()
                    .ifPresent(q -> {
                        Question updated = Question.builder()
                                .id(q.getId())
                                .assessmentId(q.getAssessmentId())
                                .type(q.getType())
                                .questionText(q.getQuestionText())
                                .options(q.getOptions())
                                .correctAnswer(q.getCorrectAnswer())
                                .points(q.getPoints())
                                .orderIndex(finalI)
                                .explanation(q.getExplanation())
                                .createdAt(q.getCreatedAt())
                                .updatedAt(java.time.LocalDateTime.now())
                                .build();
                        questionRepository.save(updated);
                    });
        }
    }
}

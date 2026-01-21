package com.mzc.lms.assessment.adapter.out.persistence;

import com.mzc.lms.assessment.adapter.out.persistence.entity.QuestionEntity;
import com.mzc.lms.assessment.adapter.out.persistence.repository.QuestionJpaRepository;
import com.mzc.lms.assessment.application.port.out.QuestionRepository;
import com.mzc.lms.assessment.domain.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuestionPersistenceAdapter implements QuestionRepository {

    private final QuestionJpaRepository questionJpaRepository;

    @Override
    public Question save(Question question) {
        QuestionEntity entity = QuestionEntity.fromDomain(question);
        QuestionEntity savedEntity = questionJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Question> findById(Long id) {
        return questionJpaRepository.findById(id)
                .map(QuestionEntity::toDomain);
    }

    @Override
    public List<Question> findByAssessmentId(Long assessmentId) {
        return questionJpaRepository.findByAssessmentIdOrderByOrderIndexAsc(assessmentId).stream()
                .map(QuestionEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Question> findByAssessmentIdOrderByOrderIndex(Long assessmentId) {
        return questionJpaRepository.findByAssessmentIdOrderByOrderIndexAsc(assessmentId).stream()
                .map(QuestionEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        questionJpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByAssessmentId(Long assessmentId) {
        questionJpaRepository.deleteByAssessmentId(assessmentId);
    }
}

package com.mzc.lms.assessment.adapter.out.persistence;

import com.mzc.lms.assessment.adapter.out.persistence.entity.SubmissionEntity;
import com.mzc.lms.assessment.adapter.out.persistence.repository.SubmissionJpaRepository;
import com.mzc.lms.assessment.application.port.out.SubmissionRepository;
import com.mzc.lms.assessment.domain.model.Submission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubmissionPersistenceAdapter implements SubmissionRepository {

    private final SubmissionJpaRepository submissionJpaRepository;

    @Override
    public Submission save(Submission submission) {
        SubmissionEntity entity = SubmissionEntity.fromDomain(submission);
        SubmissionEntity savedEntity = submissionJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Submission> findById(Long id) {
        return submissionJpaRepository.findById(id)
                .map(SubmissionEntity::toDomain);
    }

    @Override
    public List<Submission> findByAssessmentIdAndStudentId(Long assessmentId, Long studentId) {
        return submissionJpaRepository.findByAssessmentIdAndStudentIdOrderByAttemptNumberDesc(assessmentId, studentId).stream()
                .map(SubmissionEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Submission> findByAssessmentId(Long assessmentId) {
        return submissionJpaRepository.findByAssessmentId(assessmentId).stream()
                .map(SubmissionEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Submission> findByStudentId(Long studentId) {
        return submissionJpaRepository.findByStudentId(studentId).stream()
                .map(SubmissionEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Integer countByAssessmentIdAndStudentId(Long assessmentId, Long studentId) {
        return submissionJpaRepository.countByAssessmentIdAndStudentId(assessmentId, studentId);
    }

    @Override
    public void deleteById(Long id) {
        submissionJpaRepository.deleteById(id);
    }
}

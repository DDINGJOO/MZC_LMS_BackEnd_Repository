package com.mzc.lms.assessment.application.service;

import com.mzc.lms.assessment.application.port.in.AssessmentUseCase;
import com.mzc.lms.assessment.application.port.out.AssessmentEventPublisher;
import com.mzc.lms.assessment.application.port.out.AssessmentRepository;
import com.mzc.lms.assessment.domain.event.AssessmentEvent;
import com.mzc.lms.assessment.domain.model.Assessment;
import com.mzc.lms.assessment.domain.model.AssessmentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AssessmentService implements AssessmentUseCase {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentEventPublisher eventPublisher;

    @Override
    @CacheEvict(value = "assessments-by-course", key = "#courseId")
    public Assessment createAssessment(Long courseId, String title, String description,
                                        AssessmentType type, Integer totalPoints, Integer passingPoints) {
        log.info("Creating assessment for course: {}, title: {}", courseId, title);

        Assessment assessment = Assessment.create(courseId, title, description, type, totalPoints, passingPoints);
        Assessment saved = assessmentRepository.save(assessment);

        log.info("Assessment created with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "assessment", key = "#id"),
            @CacheEvict(value = "assessments-by-course", allEntries = true)
    })
    public Assessment updateAssessment(Long id, String title, String description,
                                        Integer totalPoints, Integer passingPoints, Integer timeLimitMinutes) {
        log.info("Updating assessment: {}", id);

        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found: " + id));

        Assessment updated = assessment.update(title, description, totalPoints, passingPoints, timeLimitMinutes);
        return assessmentRepository.save(updated);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "assessment", key = "#id"),
            @CacheEvict(value = "assessments-by-course", allEntries = true)
    })
    public Assessment publishAssessment(Long id) {
        log.info("Publishing assessment: {}", id);

        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found: " + id));

        Assessment published = assessment.publish();
        Assessment saved = assessmentRepository.save(published);

        eventPublisher.publish(AssessmentEvent.published(saved.getId(), saved.getCourseId(), saved.getType()));

        log.info("Assessment published: {}", id);
        return saved;
    }

    @Override
    @CacheEvict(value = "assessment", key = "#id")
    public Assessment setSchedule(Long id, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Setting schedule for assessment: {}, start: {}, end: {}", id, startDate, endDate);

        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found: " + id));

        Assessment scheduled = assessment.setSchedule(startDate, endDate);
        return assessmentRepository.save(scheduled);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "assessment", key = "#id")
    public Optional<Assessment> findById(Long id) {
        return assessmentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "assessments-by-course", key = "#courseId")
    public List<Assessment> findByCourseId(Long courseId) {
        return assessmentRepository.findByCourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Assessment> findByCourseIdAndType(Long courseId, AssessmentType type) {
        return assessmentRepository.findByCourseIdAndType(courseId, type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Assessment> findAvailableAssessments(Long courseId) {
        return assessmentRepository.findByCourseIdAndIsPublishedTrue(courseId)
                .stream()
                .filter(Assessment::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "assessment", key = "#id"),
            @CacheEvict(value = "assessments-by-course", allEntries = true)
    })
    public void deleteAssessment(Long id) {
        log.info("Deleting assessment: {}", id);
        assessmentRepository.deleteById(id);
    }
}

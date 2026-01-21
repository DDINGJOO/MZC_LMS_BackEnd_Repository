package com.mzc.lms.assessment.application.port.in;

import com.mzc.lms.assessment.domain.model.Assessment;
import com.mzc.lms.assessment.domain.model.AssessmentType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AssessmentUseCase {

    Assessment createAssessment(Long courseId, String title, String description,
                                AssessmentType type, Integer totalPoints, Integer passingPoints);

    Assessment updateAssessment(Long id, String title, String description,
                                Integer totalPoints, Integer passingPoints, Integer timeLimitMinutes);

    Assessment publishAssessment(Long id);

    Assessment setSchedule(Long id, LocalDateTime startDate, LocalDateTime endDate);

    Optional<Assessment> findById(Long id);

    List<Assessment> findByCourseId(Long courseId);

    List<Assessment> findByCourseIdAndType(Long courseId, AssessmentType type);

    List<Assessment> findAvailableAssessments(Long courseId);

    void deleteAssessment(Long id);
}

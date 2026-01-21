package com.mzc.lms.assessment.application.port.out;

import com.mzc.lms.assessment.domain.model.Assessment;
import com.mzc.lms.assessment.domain.model.AssessmentType;

import java.util.List;
import java.util.Optional;

public interface AssessmentRepository {

    Assessment save(Assessment assessment);

    Optional<Assessment> findById(Long id);

    List<Assessment> findByCourseId(Long courseId);

    List<Assessment> findByCourseIdAndType(Long courseId, AssessmentType type);

    List<Assessment> findByCourseIdAndIsPublishedTrue(Long courseId);

    void deleteById(Long id);
}

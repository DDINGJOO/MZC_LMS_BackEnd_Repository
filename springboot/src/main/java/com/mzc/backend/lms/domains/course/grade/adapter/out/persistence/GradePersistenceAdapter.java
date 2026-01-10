package com.mzc.backend.lms.domains.course.grade.adapter.out.persistence;

import com.mzc.backend.lms.domains.course.grade.application.port.out.GradeRepositoryPort;
import com.mzc.backend.lms.domains.course.grade.adapter.out.persistence.entity.Grade;
import com.mzc.backend.lms.domains.course.grade.adapter.out.persistence.repository.GradeRepository;
import com.mzc.backend.lms.domains.course.grade.domain.enums.GradeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 성적 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class GradePersistenceAdapter implements GradeRepositoryPort {

    private final GradeRepository gradeRepository;

    @Override
    public Grade save(Grade grade) {
        return gradeRepository.save(grade);
    }

    @Override
    public Optional<Grade> findByCourseIdAndStudentId(Long courseId, Long studentId) {
        return gradeRepository.findByCourseIdAndStudentId(courseId, studentId);
    }

    @Override
    public List<Grade> findByCourseIdAndStudentIdIn(Long courseId, List<Long> studentIds) {
        return gradeRepository.findByCourseIdAndStudentIdIn(courseId, studentIds);
    }

    @Override
    public List<Grade> findByCourseIdAndStudentIdInAndStatus(Long courseId, List<Long> studentIds, GradeStatus status) {
        return gradeRepository.findByCourseIdAndStudentIdInAndStatus(courseId, studentIds, status);
    }

    @Override
    public List<Grade> findByStudentIdAndStatusOrderByAcademicTermIdDescCourseIdAsc(Long studentId, GradeStatus status) {
        return gradeRepository.findByStudentIdAndStatusOrderByAcademicTermIdDescCourseIdAsc(studentId, status);
    }

    @Override
    public List<Grade> findByStudentIdAndAcademicTermIdAndStatusOrderByCourseIdAsc(Long studentId, Long academicTermId, GradeStatus status) {
        return gradeRepository.findByStudentIdAndAcademicTermIdAndStatusOrderByCourseIdAsc(studentId, academicTermId, status);
    }
}

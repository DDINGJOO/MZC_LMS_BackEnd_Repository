package com.mzc.backend.lms.domains.attendance.adapter.out.persistence;

import com.mzc.backend.lms.domains.attendance.adapter.out.persistence.mapper.StudentContentProgressMapper;
import com.mzc.backend.lms.domains.attendance.adapter.out.persistence.repository.StudentContentProgressJpaRepository;
import com.mzc.backend.lms.domains.attendance.application.port.out.StudentContentProgressRepositoryPort;
import com.mzc.backend.lms.domains.attendance.domain.model.StudentContentProgressDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * StudentContentProgress 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class StudentContentProgressPersistenceAdapter implements StudentContentProgressRepositoryPort {

    private final StudentContentProgressJpaRepository studentContentProgressJpaRepository;

    @Override
    public StudentContentProgressDomain save(StudentContentProgressDomain progress) {
        throw new UnsupportedOperationException("StudentContentProgress save operation requires Entity conversion - not yet implemented");
    }

    @Override
    public Optional<StudentContentProgressDomain> findById(Long id) {
        return studentContentProgressJpaRepository.findById(id)
                .map(StudentContentProgressMapper::toDomain);
    }

    @Override
    public Optional<StudentContentProgressDomain> findByStudentIdAndContentId(Long studentId, Long contentId) {
        return studentContentProgressJpaRepository.findByStudentStudentIdAndContentId(studentId, contentId)
                .map(StudentContentProgressMapper::toDomain);
    }

    @Override
    public List<StudentContentProgressDomain> findByStudentIdAndContentIdIn(Long studentId, List<Long> contentIds) {
        return studentContentProgressJpaRepository.findByStudentStudentIdAndContentIdIn(studentId, contentIds).stream()
                .map(StudentContentProgressMapper::toDomain)
                .toList();
    }

    @Override
    public List<StudentContentProgressDomain> findCompletedByStudentAndContentIds(Long studentId, List<Long> contentIds) {
        return studentContentProgressJpaRepository.findCompletedByStudentAndContentIds(studentId, contentIds).stream()
                .map(StudentContentProgressMapper::toDomain)
                .toList();
    }

    @Override
    public int countCompletedVideosByStudentAndWeek(Long studentId, Long weekId) {
        return studentContentProgressJpaRepository.countCompletedVideosByStudentAndWeek(studentId, weekId);
    }

    @Override
    public List<StudentContentProgressDomain> findByStudentAndCourse(Long studentId, Long courseId) {
        return studentContentProgressJpaRepository.findByStudentAndCourse(studentId, courseId).stream()
                .map(StudentContentProgressMapper::toDomain)
                .toList();
    }
}

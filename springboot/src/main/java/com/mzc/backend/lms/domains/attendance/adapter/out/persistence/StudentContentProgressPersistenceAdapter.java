package com.mzc.backend.lms.domains.attendance.adapter.out.persistence;

import com.mzc.backend.lms.domains.attendance.adapter.out.persistence.entity.StudentContentProgress;
import com.mzc.backend.lms.domains.attendance.adapter.out.persistence.repository.StudentContentProgressJpaRepository;
import com.mzc.backend.lms.domains.attendance.application.port.out.StudentContentProgressRepositoryPort;
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
    public StudentContentProgress save(StudentContentProgress progress) {
        return studentContentProgressJpaRepository.save(progress);
    }

    @Override
    public Optional<StudentContentProgress> findById(Long id) {
        return studentContentProgressJpaRepository.findById(id);
    }

    @Override
    public Optional<StudentContentProgress> findByStudentStudentIdAndContent_Id(Long studentId, Long contentId) {
        return studentContentProgressJpaRepository.findByStudentStudentIdAndContent_Id(studentId, contentId);
    }

    @Override
    public List<StudentContentProgress> findByStudentStudentIdAndContent_IdIn(Long studentId, List<Long> contentIds) {
        return studentContentProgressJpaRepository.findByStudentStudentIdAndContent_IdIn(studentId, contentIds);
    }

    @Override
    public List<StudentContentProgress> findCompletedByStudentAndContentIds(Long studentId, List<Long> contentIds) {
        return studentContentProgressJpaRepository.findCompletedByStudentAndContentIds(studentId, contentIds);
    }

    @Override
    public int countCompletedVideosByStudentAndWeek(Long studentId, Long weekId) {
        return studentContentProgressJpaRepository.countCompletedVideosByStudentAndWeek(studentId, weekId);
    }

    @Override
    public List<StudentContentProgress> findByStudentAndCourse(Long studentId, Long courseId) {
        return studentContentProgressJpaRepository.findByStudentAndCourse(studentId, courseId);
    }
}

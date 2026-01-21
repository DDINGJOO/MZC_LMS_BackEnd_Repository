package com.mzc.lms.enrollment.adapter.out.persistence;

import com.mzc.lms.enrollment.adapter.out.persistence.entity.EnrollmentEntity;
import com.mzc.lms.enrollment.adapter.out.persistence.repository.EnrollmentJpaRepository;
import com.mzc.lms.enrollment.application.port.out.EnrollmentRepository;
import com.mzc.lms.enrollment.domain.model.Enrollment;
import com.mzc.lms.enrollment.domain.model.EnrollmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EnrollmentPersistenceAdapter implements EnrollmentRepository {

    private final EnrollmentJpaRepository jpaRepository;

    @Override
    public Enrollment save(Enrollment enrollment) {
        EnrollmentEntity entity = EnrollmentEntity.fromDomain(enrollment);
        EnrollmentEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Enrollment> findById(Long id) {
        return jpaRepository.findById(id).map(EnrollmentEntity::toDomain);
    }

    @Override
    public List<Enrollment> findByStudentId(Long studentId) {
        return jpaRepository.findByStudentId(studentId)
                .stream()
                .map(EnrollmentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Enrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status) {
        return jpaRepository.findByStudentIdAndStatus(studentId, status)
                .stream()
                .map(EnrollmentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Enrollment> findByCourseId(Long courseId) {
        return jpaRepository.findByCourseId(courseId)
                .stream()
                .map(EnrollmentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Enrollment> findByCourseIdAndStatus(Long courseId, EnrollmentStatus status) {
        return jpaRepository.findByCourseIdAndStatus(courseId, status)
                .stream()
                .map(EnrollmentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId) {
        return jpaRepository.findByStudentIdAndCourseId(studentId, courseId)
                .map(EnrollmentEntity::toDomain);
    }

    @Override
    public boolean existsByStudentIdAndCourseIdAndStatusIn(Long studentId, Long courseId, List<EnrollmentStatus> statuses) {
        return jpaRepository.existsByStudentIdAndCourseIdAndStatusIn(studentId, courseId, statuses);
    }

    @Override
    public long countByCourseIdAndStatus(Long courseId, EnrollmentStatus status) {
        return jpaRepository.countByCourseIdAndStatus(courseId, status);
    }

    @Override
    public List<Enrollment> findByCourseIdAndStatusOrderByCreatedAtAsc(Long courseId, EnrollmentStatus status) {
        return jpaRepository.findByCourseIdAndStatusOrderByCreatedAtAsc(courseId, status)
                .stream()
                .map(EnrollmentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

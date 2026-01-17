package com.mzc.backend.lms.domains.enrollment.adapter.out.persistence;

import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentRepositoryPort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentRepositoryPort.EnrollmentInfo;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity.Enrollment;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.repository.EnrollmentRepository;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Student;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.StudentRepository;
import com.mzc.backend.lms.domains.enrollment.domain.exception.EnrollmentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Enrollment 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class EnrollmentPersistenceAdapter implements EnrollmentRepositoryPort {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;

    @Override
    public Long save(EnrollmentInfo enrollmentInfo) {
        Student student = studentRepository.findById(enrollmentInfo.studentId())
                .orElseThrow(() -> EnrollmentException.studentNotFound(enrollmentInfo.studentId()));

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .courseId(enrollmentInfo.courseId())
                .enrolledAt(enrollmentInfo.enrolledAt())
                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return savedEnrollment.getId();
    }

    @Override
    public Long saveWithStudentId(Long studentId, Long courseId, LocalDateTime enrolledAt) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> EnrollmentException.studentNotFound(studentId));

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .courseId(courseId)
                .enrolledAt(enrolledAt)
                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return savedEnrollment.getId();
    }

    @Override
    public Optional<EnrollmentInfo> findById(Long id) {
        return enrollmentRepository.findById(id)
                .map(this::toEnrollmentInfo);
    }

    @Override
    public List<EnrollmentInfo> findByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(this::toEnrollmentInfo)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentInfo> findByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(this::toEnrollmentInfo)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentInfo> findByStudentIdAndAcademicTermId(Long studentId, Long academicTermId) {
        return enrollmentRepository.findByStudentIdAndAcademicTermId(studentId, academicTermId).stream()
                .map(this::toEnrollmentInfo)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByStudentIdAndCourseId(Long studentId, Long courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public void delete(EnrollmentInfo enrollment) {
        enrollmentRepository.deleteById(enrollment.id());
    }

    @Override
    public void deleteById(Long id) {
        enrollmentRepository.deleteById(id);
    }

    private EnrollmentInfo toEnrollmentInfo(Enrollment enrollment) {
        return new EnrollmentInfo(
            enrollment.getId(),
            enrollment.getStudent().getStudentId(),
            enrollment.getCourseId(),
            enrollment.getEnrolledAt()
        );
    }
}

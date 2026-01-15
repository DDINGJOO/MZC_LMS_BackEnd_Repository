package com.mzc.backend.lms.domains.enrollment.adapter.out.persistence;

import com.mzc.backend.lms.domains.course.course.application.port.out.CourseRepositoryPort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentRepositoryPort;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity.Enrollment;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Enrollment 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class EnrollmentPersistenceAdapter implements EnrollmentRepositoryPort {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepositoryPort courseRepositoryPort;

    @Override
    public Enrollment save(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Optional<Enrollment> findById(Long id) {
        return enrollmentRepository.findById(id);
    }

    @Override
    public List<Enrollment> findByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    @Override
    public List<Enrollment> findByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    @Override
    public List<Enrollment> findByStudentIdAndAcademicTermId(Long studentId, Long academicTermId) {
        // MSA 전환 대비: Course는 별도 조회
        // 해당 학기의 Course ID 목록 조회
        Set<Long> termCourseIds = courseRepositoryPort.findByAcademicTermId(academicTermId).stream()
                .map(course -> course.getId())
                .collect(Collectors.toSet());

        // 학생의 수강신청 중 해당 학기 강의만 필터링
        return enrollmentRepository.findByStudentId(studentId).stream()
                .filter(e -> termCourseIds.contains(e.getCourseId()))
                .toList();
    }

    @Override
    public boolean existsByStudentIdAndCourseId(Long studentId, Long courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public void delete(Enrollment enrollment) {
        enrollmentRepository.delete(enrollment);
    }

    @Override
    public void deleteById(Long id) {
        enrollmentRepository.deleteById(id);
    }
}

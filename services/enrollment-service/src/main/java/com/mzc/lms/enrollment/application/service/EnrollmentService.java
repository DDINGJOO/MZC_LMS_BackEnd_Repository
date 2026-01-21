package com.mzc.lms.enrollment.application.service;

import com.mzc.lms.enrollment.application.port.in.EnrollmentUseCase;
import com.mzc.lms.enrollment.application.port.out.EnrollmentEventPublisher;
import com.mzc.lms.enrollment.application.port.out.EnrollmentRepository;
import com.mzc.lms.enrollment.domain.event.EnrollmentEvent;
import com.mzc.lms.enrollment.domain.model.Enrollment;
import com.mzc.lms.enrollment.domain.model.EnrollmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService implements EnrollmentUseCase {

    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentEventPublisher eventPublisher;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "enrollments-by-student", key = "#studentId"),
            @CacheEvict(value = "enrollments-by-course", key = "#courseId")
    })
    public Enrollment createEnrollment(Long studentId, Long courseId) {
        log.info("Creating enrollment for student: {} and course: {}", studentId, courseId);

        List<EnrollmentStatus> activeStatuses = List.of(
                EnrollmentStatus.PENDING,
                EnrollmentStatus.ENROLLED,
                EnrollmentStatus.WAITLISTED
        );

        if (enrollmentRepository.existsByStudentIdAndCourseIdAndStatusIn(studentId, courseId, activeStatuses)) {
            throw new IllegalStateException("Student already has an active enrollment for this course");
        }

        Enrollment enrollment = Enrollment.create(studentId, courseId);
        Enrollment saved = enrollmentRepository.save(enrollment);

        eventPublisher.publish(EnrollmentEvent.created(saved.getId(), studentId, courseId));

        log.info("Enrollment created with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "enrollment", key = "#enrollmentId"),
            @CacheEvict(value = "enrollments-by-student", allEntries = true),
            @CacheEvict(value = "enrollments-by-course", allEntries = true)
    })
    public Enrollment confirmEnrollment(Long enrollmentId) {
        log.info("Confirming enrollment: {}", enrollmentId);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + enrollmentId));

        Enrollment enrolled = enrollment.enroll();
        Enrollment saved = enrollmentRepository.save(enrolled);

        eventPublisher.publish(EnrollmentEvent.enrolled(saved.getId(), saved.getStudentId(), saved.getCourseId()));

        log.info("Enrollment confirmed: {}", enrollmentId);
        return saved;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "enrollment", key = "#enrollmentId"),
            @CacheEvict(value = "enrollments-by-student", allEntries = true),
            @CacheEvict(value = "enrollments-by-course", allEntries = true)
    })
    public Enrollment waitlistEnrollment(Long enrollmentId) {
        log.info("Waitlisting enrollment: {}", enrollmentId);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + enrollmentId));

        Enrollment waitlisted = enrollment.waitlist();
        Enrollment saved = enrollmentRepository.save(waitlisted);

        eventPublisher.publish(EnrollmentEvent.waitlisted(saved.getId(), saved.getStudentId(), saved.getCourseId()));

        log.info("Enrollment waitlisted: {}", enrollmentId);
        return saved;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "enrollment", key = "#enrollmentId"),
            @CacheEvict(value = "enrollments-by-student", allEntries = true),
            @CacheEvict(value = "enrollments-by-course", allEntries = true)
    })
    public Enrollment withdrawEnrollment(Long enrollmentId, String reason) {
        log.info("Withdrawing enrollment: {} with reason: {}", enrollmentId, reason);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + enrollmentId));

        EnrollmentStatus previousStatus = enrollment.getStatus();
        Enrollment withdrawn = enrollment.withdraw(reason);
        Enrollment saved = enrollmentRepository.save(withdrawn);

        eventPublisher.publish(EnrollmentEvent.withdrawn(saved.getId(), saved.getStudentId(), saved.getCourseId(), previousStatus));

        log.info("Enrollment withdrawn: {}", enrollmentId);
        return saved;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "enrollment", key = "#enrollmentId"),
            @CacheEvict(value = "enrollments-by-student", allEntries = true),
            @CacheEvict(value = "enrollments-by-course", allEntries = true)
    })
    public Enrollment completeEnrollment(Long enrollmentId, Integer grade, String gradePoint) {
        log.info("Completing enrollment: {} with grade: {}", enrollmentId, grade);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + enrollmentId));

        Enrollment completed = enrollment.complete(grade, gradePoint);
        Enrollment saved = enrollmentRepository.save(completed);

        eventPublisher.publish(EnrollmentEvent.completed(saved.getId(), saved.getStudentId(), saved.getCourseId(), saved.getStatus()));

        log.info("Enrollment completed: {} with status: {}", enrollmentId, saved.getStatus());
        return saved;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "enrollment", key = "#enrollmentId"),
            @CacheEvict(value = "enrollments-by-student", allEntries = true),
            @CacheEvict(value = "enrollments-by-course", allEntries = true)
    })
    public Enrollment cancelEnrollment(Long enrollmentId) {
        log.info("Cancelling enrollment: {}", enrollmentId);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + enrollmentId));

        EnrollmentStatus previousStatus = enrollment.getStatus();
        Enrollment cancelled = enrollment.cancel();
        Enrollment saved = enrollmentRepository.save(cancelled);

        eventPublisher.publish(EnrollmentEvent.cancelled(saved.getId(), saved.getStudentId(), saved.getCourseId(), previousStatus));

        log.info("Enrollment cancelled: {}", enrollmentId);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "enrollment", key = "#id")
    public Optional<Enrollment> findById(Long id) {
        return enrollmentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "enrollments-by-student", key = "#studentId")
    public List<Enrollment> findByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status) {
        return enrollmentRepository.findByStudentIdAndStatus(studentId, status);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "enrollments-by-course", key = "#courseId")
    public List<Enrollment> findByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enrollment> findByCourseIdAndStatus(Long courseId, EnrollmentStatus status) {
        return enrollmentRepository.findByCourseIdAndStatus(courseId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId) {
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isStudentEnrolled(Long studentId, Long courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseIdAndStatusIn(
                studentId, courseId, List.of(EnrollmentStatus.ENROLLED));
    }

    @Override
    @Transactional(readOnly = true)
    public long countByCourseIdAndStatus(Long courseId, EnrollmentStatus status) {
        return enrollmentRepository.countByCourseIdAndStatus(courseId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enrollment> findWaitlistedByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseIdAndStatusOrderByCreatedAtAsc(courseId, EnrollmentStatus.WAITLISTED);
    }
}

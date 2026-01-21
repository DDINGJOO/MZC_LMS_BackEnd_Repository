package com.mzc.lms.enrollment.domain.event;

import com.mzc.lms.enrollment.domain.model.EnrollmentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EnrollmentEvent {
    private String eventId;
    private String eventType;
    private Long enrollmentId;
    private Long studentId;
    private Long courseId;
    private EnrollmentStatus previousStatus;
    private EnrollmentStatus currentStatus;
    private LocalDateTime occurredAt;

    public static EnrollmentEvent created(Long enrollmentId, Long studentId, Long courseId) {
        return EnrollmentEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("ENROLLMENT_CREATED")
                .enrollmentId(enrollmentId)
                .studentId(studentId)
                .courseId(courseId)
                .currentStatus(EnrollmentStatus.PENDING)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static EnrollmentEvent enrolled(Long enrollmentId, Long studentId, Long courseId) {
        return EnrollmentEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("ENROLLMENT_CONFIRMED")
                .enrollmentId(enrollmentId)
                .studentId(studentId)
                .courseId(courseId)
                .previousStatus(EnrollmentStatus.PENDING)
                .currentStatus(EnrollmentStatus.ENROLLED)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static EnrollmentEvent waitlisted(Long enrollmentId, Long studentId, Long courseId) {
        return EnrollmentEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("ENROLLMENT_WAITLISTED")
                .enrollmentId(enrollmentId)
                .studentId(studentId)
                .courseId(courseId)
                .previousStatus(EnrollmentStatus.PENDING)
                .currentStatus(EnrollmentStatus.WAITLISTED)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static EnrollmentEvent withdrawn(Long enrollmentId, Long studentId, Long courseId, EnrollmentStatus previousStatus) {
        return EnrollmentEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("ENROLLMENT_WITHDRAWN")
                .enrollmentId(enrollmentId)
                .studentId(studentId)
                .courseId(courseId)
                .previousStatus(previousStatus)
                .currentStatus(EnrollmentStatus.WITHDRAWN)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static EnrollmentEvent completed(Long enrollmentId, Long studentId, Long courseId, EnrollmentStatus status) {
        return EnrollmentEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("ENROLLMENT_COMPLETED")
                .enrollmentId(enrollmentId)
                .studentId(studentId)
                .courseId(courseId)
                .previousStatus(EnrollmentStatus.ENROLLED)
                .currentStatus(status)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static EnrollmentEvent cancelled(Long enrollmentId, Long studentId, Long courseId, EnrollmentStatus previousStatus) {
        return EnrollmentEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("ENROLLMENT_CANCELLED")
                .enrollmentId(enrollmentId)
                .studentId(studentId)
                .courseId(courseId)
                .previousStatus(previousStatus)
                .currentStatus(EnrollmentStatus.CANCELLED)
                .occurredAt(LocalDateTime.now())
                .build();
    }
}

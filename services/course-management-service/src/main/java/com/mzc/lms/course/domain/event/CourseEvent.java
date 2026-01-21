package com.mzc.lms.course.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CourseEvent {

    private String eventType;
    private String entityType;
    private Long entityId;
    private Object payload;
    private LocalDateTime occurredAt;

    public static final String COURSE_CREATED = "COURSE_CREATED";
    public static final String COURSE_UPDATED = "COURSE_UPDATED";
    public static final String COURSE_DEACTIVATED = "COURSE_DEACTIVATED";
    public static final String COURSE_STUDENT_ENROLLED = "COURSE_STUDENT_ENROLLED";
    public static final String COURSE_STUDENT_DROPPED = "COURSE_STUDENT_DROPPED";
    public static final String SCHEDULE_ADDED = "SCHEDULE_ADDED";
    public static final String SCHEDULE_UPDATED = "SCHEDULE_UPDATED";
    public static final String SCHEDULE_REMOVED = "SCHEDULE_REMOVED";

    public static final String ENTITY_COURSE = "COURSE";
    public static final String ENTITY_SCHEDULE = "SCHEDULE";

    public static CourseEvent courseCreated(Long courseId, Object payload) {
        return CourseEvent.builder()
                .eventType(COURSE_CREATED)
                .entityType(ENTITY_COURSE)
                .entityId(courseId)
                .payload(payload)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static CourseEvent courseUpdated(Long courseId, Object payload) {
        return CourseEvent.builder()
                .eventType(COURSE_UPDATED)
                .entityType(ENTITY_COURSE)
                .entityId(courseId)
                .payload(payload)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static CourseEvent courseDeactivated(Long courseId) {
        return CourseEvent.builder()
                .eventType(COURSE_DEACTIVATED)
                .entityType(ENTITY_COURSE)
                .entityId(courseId)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static CourseEvent studentEnrolled(Long courseId, Long studentId) {
        return CourseEvent.builder()
                .eventType(COURSE_STUDENT_ENROLLED)
                .entityType(ENTITY_COURSE)
                .entityId(courseId)
                .payload(studentId)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static CourseEvent studentDropped(Long courseId, Long studentId) {
        return CourseEvent.builder()
                .eventType(COURSE_STUDENT_DROPPED)
                .entityType(ENTITY_COURSE)
                .entityId(courseId)
                .payload(studentId)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static CourseEvent scheduleAdded(Long courseId, Long scheduleId) {
        return CourseEvent.builder()
                .eventType(SCHEDULE_ADDED)
                .entityType(ENTITY_SCHEDULE)
                .entityId(courseId)
                .payload(scheduleId)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static CourseEvent scheduleUpdated(Long scheduleId, Object payload) {
        return CourseEvent.builder()
                .eventType(SCHEDULE_UPDATED)
                .entityType(ENTITY_SCHEDULE)
                .entityId(scheduleId)
                .payload(payload)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static CourseEvent scheduleRemoved(Long courseId, Long scheduleId) {
        return CourseEvent.builder()
                .eventType(SCHEDULE_REMOVED)
                .entityType(ENTITY_SCHEDULE)
                .entityId(courseId)
                .payload(scheduleId)
                .occurredAt(LocalDateTime.now())
                .build();
    }
}

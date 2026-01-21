package com.mzc.lms.catalog.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CatalogEvent {

    private String eventType;
    private String entityType;
    private Long entityId;
    private Object payload;
    private LocalDateTime occurredAt;

    public static final String SUBJECT_CREATED = "SUBJECT_CREATED";
    public static final String SUBJECT_UPDATED = "SUBJECT_UPDATED";
    public static final String SUBJECT_DEACTIVATED = "SUBJECT_DEACTIVATED";
    public static final String SUBJECT_ACTIVATED = "SUBJECT_ACTIVATED";
    public static final String PREREQUISITE_ADDED = "PREREQUISITE_ADDED";
    public static final String PREREQUISITE_REMOVED = "PREREQUISITE_REMOVED";
    public static final String COURSE_TYPE_CREATED = "COURSE_TYPE_CREATED";
    public static final String COURSE_TYPE_UPDATED = "COURSE_TYPE_UPDATED";

    public static final String ENTITY_SUBJECT = "SUBJECT";
    public static final String ENTITY_COURSE_TYPE = "COURSE_TYPE";
    public static final String ENTITY_PREREQUISITE = "PREREQUISITE";

    public static CatalogEvent subjectCreated(Long subjectId, Object payload) {
        return CatalogEvent.builder()
                .eventType(SUBJECT_CREATED)
                .entityType(ENTITY_SUBJECT)
                .entityId(subjectId)
                .payload(payload)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static CatalogEvent subjectUpdated(Long subjectId, Object payload) {
        return CatalogEvent.builder()
                .eventType(SUBJECT_UPDATED)
                .entityType(ENTITY_SUBJECT)
                .entityId(subjectId)
                .payload(payload)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static CatalogEvent subjectDeactivated(Long subjectId) {
        return CatalogEvent.builder()
                .eventType(SUBJECT_DEACTIVATED)
                .entityType(ENTITY_SUBJECT)
                .entityId(subjectId)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static CatalogEvent subjectActivated(Long subjectId) {
        return CatalogEvent.builder()
                .eventType(SUBJECT_ACTIVATED)
                .entityType(ENTITY_SUBJECT)
                .entityId(subjectId)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static CatalogEvent prerequisiteAdded(Long subjectId, Long prerequisiteId) {
        return CatalogEvent.builder()
                .eventType(PREREQUISITE_ADDED)
                .entityType(ENTITY_PREREQUISITE)
                .entityId(subjectId)
                .payload(prerequisiteId)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static CatalogEvent prerequisiteRemoved(Long subjectId, Long prerequisiteId) {
        return CatalogEvent.builder()
                .eventType(PREREQUISITE_REMOVED)
                .entityType(ENTITY_PREREQUISITE)
                .entityId(subjectId)
                .payload(prerequisiteId)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static CatalogEvent courseTypeCreated(Long courseTypeId, Object payload) {
        return CatalogEvent.builder()
                .eventType(COURSE_TYPE_CREATED)
                .entityType(ENTITY_COURSE_TYPE)
                .entityId(courseTypeId)
                .payload(payload)
                .occurredAt(LocalDateTime.now())
                .build();
    }
}

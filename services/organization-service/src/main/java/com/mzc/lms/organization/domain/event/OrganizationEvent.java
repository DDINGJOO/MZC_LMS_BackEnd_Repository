package com.mzc.lms.organization.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationEvent {

    private String eventType;
    private String entityType;
    private Long entityId;
    private String entityName;
    private LocalDateTime occurredAt;

    public static OrganizationEvent collegeCreated(Long collegeId, String collegeName) {
        return OrganizationEvent.builder()
                .eventType("COLLEGE_CREATED")
                .entityType("COLLEGE")
                .entityId(collegeId)
                .entityName(collegeName)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static OrganizationEvent departmentCreated(Long departmentId, String departmentName) {
        return OrganizationEvent.builder()
                .eventType("DEPARTMENT_CREATED")
                .entityType("DEPARTMENT")
                .entityId(departmentId)
                .entityName(departmentName)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public static OrganizationEvent academicTermCreated(Long termId, String termName) {
        return OrganizationEvent.builder()
                .eventType("ACADEMIC_TERM_CREATED")
                .entityType("ACADEMIC_TERM")
                .entityId(termId)
                .entityName(termName)
                .occurredAt(LocalDateTime.now())
                .build();
    }
}

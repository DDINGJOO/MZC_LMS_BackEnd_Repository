package com.mzc.lms.catalog.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SubjectPrerequisite {

    private Long id;
    private Long subjectId;
    private Long prerequisiteId;
    private Boolean isMandatory;
    private LocalDateTime createdAt;

    public static SubjectPrerequisite create(
            Long subjectId,
            Long prerequisiteId,
            Boolean isMandatory
    ) {
        return SubjectPrerequisite.builder()
                .subjectId(subjectId)
                .prerequisiteId(prerequisiteId)
                .isMandatory(isMandatory != null ? isMandatory : true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public boolean isRequired() {
        return Boolean.TRUE.equals(isMandatory);
    }

    public boolean isRecommended() {
        return Boolean.FALSE.equals(isMandatory);
    }
}

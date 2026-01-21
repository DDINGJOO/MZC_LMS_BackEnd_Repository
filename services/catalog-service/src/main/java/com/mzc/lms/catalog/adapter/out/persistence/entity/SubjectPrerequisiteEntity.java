package com.mzc.lms.catalog.adapter.out.persistence.entity;

import com.mzc.lms.catalog.domain.model.SubjectPrerequisite;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "subject_prerequisites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectPrerequisiteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    @Column(name = "prerequisite_id", nullable = false)
    private Long prerequisiteId;

    @Column(name = "is_mandatory", nullable = false)
    @Builder.Default
    private Boolean isMandatory = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public SubjectPrerequisite toDomain() {
        return SubjectPrerequisite.builder()
                .id(id)
                .subjectId(subjectId)
                .prerequisiteId(prerequisiteId)
                .isMandatory(isMandatory)
                .createdAt(createdAt)
                .build();
    }

    public static SubjectPrerequisiteEntity fromDomain(SubjectPrerequisite prerequisite) {
        return SubjectPrerequisiteEntity.builder()
                .id(prerequisite.getId())
                .subjectId(prerequisite.getSubjectId())
                .prerequisiteId(prerequisite.getPrerequisiteId())
                .isMandatory(prerequisite.getIsMandatory())
                .createdAt(prerequisite.getCreatedAt())
                .build();
    }
}

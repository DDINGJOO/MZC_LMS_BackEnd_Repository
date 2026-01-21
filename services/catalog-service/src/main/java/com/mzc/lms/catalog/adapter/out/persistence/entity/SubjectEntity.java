package com.mzc.lms.catalog.adapter.out.persistence.entity;

import com.mzc.lms.catalog.domain.model.Subject;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_code", length = 8, unique = true, nullable = false)
    private String subjectCode;

    @Column(name = "subject_name", length = 20, nullable = false)
    private String subjectName;

    @Column(name = "subject_description", length = 200, nullable = false)
    private String subjectDescription;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "course_type_id", nullable = false)
    private Long courseTypeId;

    @Column(nullable = false)
    private Integer credits;

    @Column(name = "theory_hours")
    private Integer theoryHours;

    @Column(name = "practice_hours")
    private Integer practiceHours;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Subject toDomain() {
        return Subject.builder()
                .id(id)
                .subjectCode(subjectCode)
                .subjectName(subjectName)
                .subjectDescription(subjectDescription)
                .departmentId(departmentId)
                .courseTypeId(courseTypeId)
                .credits(credits)
                .theoryHours(theoryHours)
                .practiceHours(practiceHours)
                .description(description)
                .isActive(isActive)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static SubjectEntity fromDomain(Subject subject) {
        return SubjectEntity.builder()
                .id(subject.getId())
                .subjectCode(subject.getSubjectCode())
                .subjectName(subject.getSubjectName())
                .subjectDescription(subject.getSubjectDescription())
                .departmentId(subject.getDepartmentId())
                .courseTypeId(subject.getCourseTypeId())
                .credits(subject.getCredits())
                .theoryHours(subject.getTheoryHours())
                .practiceHours(subject.getPracticeHours())
                .description(subject.getDescription())
                .isActive(subject.getIsActive())
                .createdAt(subject.getCreatedAt())
                .updatedAt(subject.getUpdatedAt())
                .build();
    }
}

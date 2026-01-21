package com.mzc.lms.catalog.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class Subject {

    private Long id;
    private String subjectCode;
    private String subjectName;
    private String subjectDescription;
    private Long departmentId;
    private Long courseTypeId;
    private Integer credits;
    private Integer theoryHours;
    private Integer practiceHours;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<SubjectPrerequisite> prerequisites = new ArrayList<>();

    public static Subject create(
            String subjectCode,
            String subjectName,
            String subjectDescription,
            Long departmentId,
            Long courseTypeId,
            Integer credits,
            Integer theoryHours,
            Integer practiceHours,
            String description
    ) {
        return Subject.builder()
                .subjectCode(subjectCode)
                .subjectName(subjectName)
                .subjectDescription(subjectDescription)
                .departmentId(departmentId)
                .courseTypeId(courseTypeId)
                .credits(credits)
                .theoryHours(theoryHours)
                .practiceHours(practiceHours)
                .description(description)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Subject update(
            String subjectName,
            String subjectDescription,
            Long courseTypeId,
            Integer credits,
            Integer theoryHours,
            Integer practiceHours,
            String description
    ) {
        return Subject.builder()
                .id(this.id)
                .subjectCode(this.subjectCode)
                .subjectName(subjectName != null ? subjectName : this.subjectName)
                .subjectDescription(subjectDescription != null ? subjectDescription : this.subjectDescription)
                .departmentId(this.departmentId)
                .courseTypeId(courseTypeId != null ? courseTypeId : this.courseTypeId)
                .credits(credits != null ? credits : this.credits)
                .theoryHours(theoryHours != null ? theoryHours : this.theoryHours)
                .practiceHours(practiceHours != null ? practiceHours : this.practiceHours)
                .description(description != null ? description : this.description)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .prerequisites(this.prerequisites)
                .build();
    }

    public Subject deactivate() {
        return Subject.builder()
                .id(this.id)
                .subjectCode(this.subjectCode)
                .subjectName(this.subjectName)
                .subjectDescription(this.subjectDescription)
                .departmentId(this.departmentId)
                .courseTypeId(this.courseTypeId)
                .credits(this.credits)
                .theoryHours(this.theoryHours)
                .practiceHours(this.practiceHours)
                .description(this.description)
                .isActive(false)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .prerequisites(this.prerequisites)
                .build();
    }

    public Subject activate() {
        return Subject.builder()
                .id(this.id)
                .subjectCode(this.subjectCode)
                .subjectName(this.subjectName)
                .subjectDescription(this.subjectDescription)
                .departmentId(this.departmentId)
                .courseTypeId(this.courseTypeId)
                .credits(this.credits)
                .theoryHours(this.theoryHours)
                .practiceHours(this.practiceHours)
                .description(this.description)
                .isActive(true)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .prerequisites(this.prerequisites)
                .build();
    }
}

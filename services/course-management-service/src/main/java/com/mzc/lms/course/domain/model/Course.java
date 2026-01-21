package com.mzc.lms.course.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class Course {

    private Long id;
    private Long subjectId;
    private Long professorId;
    private Long academicTermId;
    private String sectionNumber;
    private Integer maxStudents;
    private Integer currentStudents;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<CourseSchedule> schedules = new ArrayList<>();

    public static Course create(
            Long subjectId,
            Long professorId,
            Long academicTermId,
            String sectionNumber,
            Integer maxStudents,
            String description
    ) {
        return Course.builder()
                .subjectId(subjectId)
                .professorId(professorId)
                .academicTermId(academicTermId)
                .sectionNumber(sectionNumber)
                .maxStudents(maxStudents)
                .currentStudents(0)
                .description(description)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Course update(
            Long professorId,
            Integer maxStudents,
            String description
    ) {
        return Course.builder()
                .id(this.id)
                .subjectId(this.subjectId)
                .professorId(professorId != null ? professorId : this.professorId)
                .academicTermId(this.academicTermId)
                .sectionNumber(this.sectionNumber)
                .maxStudents(maxStudents != null ? maxStudents : this.maxStudents)
                .currentStudents(this.currentStudents)
                .description(description != null ? description : this.description)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .schedules(this.schedules)
                .build();
    }

    public Course incrementStudents() {
        if (this.currentStudents >= this.maxStudents) {
            throw new IllegalStateException("Course is full");
        }
        return Course.builder()
                .id(this.id)
                .subjectId(this.subjectId)
                .professorId(this.professorId)
                .academicTermId(this.academicTermId)
                .sectionNumber(this.sectionNumber)
                .maxStudents(this.maxStudents)
                .currentStudents(this.currentStudents + 1)
                .description(this.description)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .schedules(this.schedules)
                .build();
    }

    public Course decrementStudents() {
        if (this.currentStudents <= 0) {
            throw new IllegalStateException("No students to remove");
        }
        return Course.builder()
                .id(this.id)
                .subjectId(this.subjectId)
                .professorId(this.professorId)
                .academicTermId(this.academicTermId)
                .sectionNumber(this.sectionNumber)
                .maxStudents(this.maxStudents)
                .currentStudents(this.currentStudents - 1)
                .description(this.description)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .schedules(this.schedules)
                .build();
    }

    public Course deactivate() {
        return Course.builder()
                .id(this.id)
                .subjectId(this.subjectId)
                .professorId(this.professorId)
                .academicTermId(this.academicTermId)
                .sectionNumber(this.sectionNumber)
                .maxStudents(this.maxStudents)
                .currentStudents(this.currentStudents)
                .description(this.description)
                .isActive(false)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .schedules(this.schedules)
                .build();
    }

    public boolean hasAvailableSeats() {
        return this.currentStudents < this.maxStudents;
    }

    public int getAvailableSeats() {
        return this.maxStudents - this.currentStudents;
    }
}

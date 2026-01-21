package com.mzc.lms.course.adapter.out.persistence.entity;

import com.mzc.lms.course.domain.model.Course;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    @Column(name = "professor_id", nullable = false)
    private Long professorId;

    @Column(name = "academic_term_id", nullable = false)
    private Long academicTermId;

    @Column(name = "section_number", nullable = false)
    private String sectionNumber;

    @Column(name = "max_students", nullable = false)
    private Integer maxStudents;

    @Column(name = "current_students", nullable = false)
    @Builder.Default
    private Integer currentStudents = 0;

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

    public Course toDomain() {
        return Course.builder()
                .id(id)
                .subjectId(subjectId)
                .professorId(professorId)
                .academicTermId(academicTermId)
                .sectionNumber(sectionNumber)
                .maxStudents(maxStudents)
                .currentStudents(currentStudents)
                .description(description)
                .isActive(isActive)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static CourseEntity fromDomain(Course course) {
        return CourseEntity.builder()
                .id(course.getId())
                .subjectId(course.getSubjectId())
                .professorId(course.getProfessorId())
                .academicTermId(course.getAcademicTermId())
                .sectionNumber(course.getSectionNumber())
                .maxStudents(course.getMaxStudents())
                .currentStudents(course.getCurrentStudents())
                .description(course.getDescription())
                .isActive(course.getIsActive())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}

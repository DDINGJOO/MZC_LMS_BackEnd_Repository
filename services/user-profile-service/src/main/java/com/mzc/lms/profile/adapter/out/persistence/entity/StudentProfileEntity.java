package com.mzc.lms.profile.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_profiles", indexes = {
    @Index(name = "idx_student_profiles_user_id", columnList = "user_id"),
    @Index(name = "idx_student_profiles_admission_year", columnList = "admission_year")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentProfileEntity {

    @Id
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "admission_year", nullable = false)
    private Integer admissionYear;

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Column(name = "department_name", length = 100)
    private String departmentName;

    @Column(name = "college_name", length = 100)
    private String collegeName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public StudentProfileEntity(Long studentId, Long userId, Integer admissionYear, Integer grade,
                                String departmentName, String collegeName) {
        this.studentId = studentId;
        this.userId = userId;
        this.admissionYear = admissionYear;
        this.grade = grade != null ? grade : 1;
        this.departmentName = departmentName;
        this.collegeName = collegeName;
    }

    public void updateGrade(Integer grade) {
        if (grade < 1 || grade > 4) {
            throw new IllegalArgumentException("Grade must be between 1 and 4");
        }
        this.grade = grade;
    }
}

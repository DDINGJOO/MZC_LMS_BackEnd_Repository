package com.mzc.lms.profile.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "professor_profiles", indexes = {
    @Index(name = "idx_professor_profiles_user_id", columnList = "user_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfessorProfileEntity {

    @Id
    @Column(name = "professor_id")
    private Long professorId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "department_name", length = 100)
    private String departmentName;

    @Column(name = "college_name", length = 100)
    private String collegeName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public ProfessorProfileEntity(Long professorId, Long userId, LocalDate appointmentDate,
                                  String departmentName, String collegeName) {
        this.professorId = professorId;
        this.userId = userId;
        this.appointmentDate = appointmentDate;
        this.departmentName = departmentName;
        this.collegeName = collegeName;
    }
}

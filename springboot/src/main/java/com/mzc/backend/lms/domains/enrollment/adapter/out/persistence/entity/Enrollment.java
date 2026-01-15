package com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

import java.time.LocalDateTime;

/**
 * 수강신청 엔티티
 * MSA 전환을 위해 다른 도메인 Entity 직접 참조 대신 ID만 저장
 */
@Entity
@Table(name = "enrollments")
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "enrolled_at", nullable = false)
    private LocalDateTime enrolledAt;

    /**
     * 팩토리 메서드
     */
    public static Enrollment create(Long courseId, Long studentId) {
        return Enrollment.builder()
                .courseId(courseId)
                .studentId(studentId)
                .enrolledAt(LocalDateTime.now())
                .build();
    }
}       
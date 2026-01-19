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
 * Enrollment 엔티티
 * enrollments 테이블과 매핑
 *
 * Student 엔티티 직접 참조 → studentId로 변경 (도메인 간 순환 의존성 방지)
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
}

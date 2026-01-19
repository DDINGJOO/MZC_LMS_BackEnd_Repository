package com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * CourseCart 엔티티
 * course_carts 테이블과 매핑
 *
 * Student 엔티티 직접 참조 → studentId로 변경 (도메인 간 순환 의존성 방지)
 */
@Entity
@Table(name = "course_carts")
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CourseCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;
}

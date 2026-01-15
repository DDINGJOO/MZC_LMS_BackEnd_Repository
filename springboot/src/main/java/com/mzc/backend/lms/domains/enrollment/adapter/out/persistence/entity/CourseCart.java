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
 * 수강 장바구니 엔티티
 * MSA 전환을 위해 다른 도메인 Entity 직접 참조 대신 ID만 저장
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

    /**
     * 팩토리 메서드
     */
    public static CourseCart create(Long studentId, Long courseId) {
        return CourseCart.builder()
                .studentId(studentId)
                .courseId(courseId)
                .addedAt(LocalDateTime.now())
                .build();
    }
}

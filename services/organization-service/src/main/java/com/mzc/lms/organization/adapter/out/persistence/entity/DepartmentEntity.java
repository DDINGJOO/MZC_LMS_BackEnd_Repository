package com.mzc.lms.organization.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "departments", indexes = {
    @Index(name = "idx_departments_department_code", columnList = "department_code"),
    @Index(name = "idx_departments_college_id", columnList = "college_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department_code", length = 20, unique = true, nullable = false)
    private String departmentCode;

    @Column(name = "department_name", length = 100, nullable = false)
    private String departmentName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id", nullable = false)
    private CollegeEntity college;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public DepartmentEntity(String departmentCode, String departmentName, CollegeEntity college) {
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
        this.college = college;
    }

    public void updateName(String name) {
        this.departmentName = name;
    }

    public void updateCode(String code) {
        this.departmentCode = code;
    }

    public void changeCollege(CollegeEntity college) {
        this.college = college;
    }
}

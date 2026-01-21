package com.mzc.lms.organization.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "colleges")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CollegeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "college_code", length = 20, unique = true, nullable = false)
    private String collegeCode;

    @Column(name = "college_number_code", length = 2, unique = true, nullable = false)
    private String collegeNumberCode;

    @Column(name = "college_name", length = 100, nullable = false)
    private String collegeName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DepartmentEntity> departments = new ArrayList<>();

    @Builder
    public CollegeEntity(String collegeCode, String collegeNumberCode, String collegeName) {
        this.collegeCode = collegeCode;
        this.collegeNumberCode = collegeNumberCode;
        this.collegeName = collegeName;
    }

    public void updateName(String name) {
        this.collegeName = name;
    }

    public void updateCode(String code) {
        this.collegeCode = code;
    }
}

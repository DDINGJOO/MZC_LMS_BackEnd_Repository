package com.mzc.lms.catalog.adapter.out.persistence.entity;

import com.mzc.lms.catalog.domain.model.CourseType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_code", unique = true, nullable = false)
    private Integer typeCode;

    @Column(name = "category", nullable = false)
    private Integer category;

    public CourseType toDomain() {
        return CourseType.builder()
                .id(id)
                .typeCode(typeCode)
                .category(category)
                .build();
    }

    public static CourseTypeEntity fromDomain(CourseType courseType) {
        return CourseTypeEntity.builder()
                .id(courseType.getId())
                .typeCode(courseType.getTypeCode())
                .category(courseType.getCategory())
                .build();
    }
}

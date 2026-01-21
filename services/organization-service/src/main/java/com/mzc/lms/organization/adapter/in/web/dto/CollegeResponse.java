package com.mzc.lms.organization.adapter.in.web.dto;

import com.mzc.lms.organization.domain.model.College;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollegeResponse {

    private Long id;
    private String collegeCode;
    private String collegeNumberCode;
    private String collegeName;
    private Integer departmentCount;
    private LocalDateTime createdAt;

    public static CollegeResponse from(College college) {
        return CollegeResponse.builder()
                .id(college.getId())
                .collegeCode(college.getCollegeCode())
                .collegeNumberCode(college.getCollegeNumberCode())
                .collegeName(college.getCollegeName())
                .departmentCount(college.getDepartments() != null ? college.getDepartments().size() : 0)
                .createdAt(college.getCreatedAt())
                .build();
    }

    public static List<CollegeResponse> from(List<College> colleges) {
        return colleges.stream()
                .map(CollegeResponse::from)
                .toList();
    }
}

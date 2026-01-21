package com.mzc.lms.organization.adapter.in.web.dto;

import com.mzc.lms.organization.domain.model.Department;
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
public class DepartmentResponse {

    private Long id;
    private String departmentCode;
    private String departmentName;
    private Long collegeId;
    private String collegeName;
    private LocalDateTime createdAt;

    public static DepartmentResponse from(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .departmentCode(department.getDepartmentCode())
                .departmentName(department.getDepartmentName())
                .collegeId(department.getCollegeId())
                .collegeName(department.getCollegeName())
                .createdAt(department.getCreatedAt())
                .build();
    }

    public static List<DepartmentResponse> from(List<Department> departments) {
        return departments.stream()
                .map(DepartmentResponse::from)
                .toList();
    }
}

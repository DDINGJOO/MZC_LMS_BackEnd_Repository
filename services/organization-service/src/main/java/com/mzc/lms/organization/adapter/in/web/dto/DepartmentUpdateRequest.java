package com.mzc.lms.organization.adapter.in.web.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentUpdateRequest {

    @Size(max = 20, message = "Department code must be at most 20 characters")
    private String departmentCode;

    @Size(max = 100, message = "Department name must be at most 100 characters")
    private String departmentName;

    private Long collegeId;
}

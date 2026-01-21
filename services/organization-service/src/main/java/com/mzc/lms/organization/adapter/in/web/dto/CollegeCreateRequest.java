package com.mzc.lms.organization.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollegeCreateRequest {

    @NotBlank(message = "College code is required")
    @Size(max = 20, message = "College code must be at most 20 characters")
    private String collegeCode;

    @NotBlank(message = "College number code is required")
    @Size(max = 2, message = "College number code must be at most 2 characters")
    private String collegeNumberCode;

    @NotBlank(message = "College name is required")
    @Size(max = 100, message = "College name must be at most 100 characters")
    private String collegeName;
}

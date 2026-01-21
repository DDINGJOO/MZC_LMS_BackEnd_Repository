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
public class CollegeUpdateRequest {

    @Size(max = 20, message = "College code must be at most 20 characters")
    private String collegeCode;

    @Size(max = 100, message = "College name must be at most 100 characters")
    private String collegeName;
}

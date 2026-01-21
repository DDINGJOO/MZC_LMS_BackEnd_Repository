package com.mzc.lms.catalog.adapter.in.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectCreateRequest {

    @NotBlank(message = "Subject code is required")
    @Size(max = 8, message = "Subject code must be at most 8 characters")
    private String subjectCode;

    @NotBlank(message = "Subject name is required")
    @Size(max = 20, message = "Subject name must be at most 20 characters")
    private String subjectName;

    @NotBlank(message = "Subject description is required")
    @Size(max = 200, message = "Subject description must be at most 200 characters")
    private String subjectDescription;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotNull(message = "Course type ID is required")
    private Long courseTypeId;

    @NotNull(message = "Credits is required")
    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 4, message = "Credits must be at most 4")
    private Integer credits;

    private Integer theoryHours;

    private Integer practiceHours;

    private String description;
}

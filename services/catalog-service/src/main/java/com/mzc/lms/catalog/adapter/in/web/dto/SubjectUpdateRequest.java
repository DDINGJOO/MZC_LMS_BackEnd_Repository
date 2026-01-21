package com.mzc.lms.catalog.adapter.in.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectUpdateRequest {

    @Size(max = 20, message = "Subject name must be at most 20 characters")
    private String subjectName;

    @Size(max = 200, message = "Subject description must be at most 200 characters")
    private String subjectDescription;

    private Long courseTypeId;

    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 4, message = "Credits must be at most 4")
    private Integer credits;

    private Integer theoryHours;

    private Integer practiceHours;

    private String description;
}

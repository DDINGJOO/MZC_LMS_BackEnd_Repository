package com.mzc.lms.course.adapter.in.web.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseUpdateRequest {

    private Long professorId;

    @Min(value = 1, message = "Max students must be at least 1")
    private Integer maxStudents;

    private String description;
}

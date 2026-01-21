package com.mzc.lms.enrollment.adapter.in.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteEnrollmentRequest {

    @Min(value = 0, message = "Grade must be at least 0")
    @Max(value = 100, message = "Grade must not exceed 100")
    private Integer grade;

    @Size(max = 10, message = "Grade point must not exceed 10 characters")
    private String gradePoint;
}

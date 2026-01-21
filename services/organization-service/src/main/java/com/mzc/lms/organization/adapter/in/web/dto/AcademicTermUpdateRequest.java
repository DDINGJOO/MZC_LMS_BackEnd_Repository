package com.mzc.lms.organization.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicTermUpdateRequest {

    private LocalDate startDate;
    private LocalDate endDate;
}

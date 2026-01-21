package com.mzc.lms.profile.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSearchRequest {

    private String name;
    private String userType;
    private String departmentName;
    private Integer grade;
    private Integer admissionYear;
}

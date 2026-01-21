package com.mzc.lms.profile.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSearchResponse {

    private Long userId;
    private String name;
    private String email;
    private String userType;
    private String profileImageUrl;
    private String departmentName;
    private Long userNumber;
}

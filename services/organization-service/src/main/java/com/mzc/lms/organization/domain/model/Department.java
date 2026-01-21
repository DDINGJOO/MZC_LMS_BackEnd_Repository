package com.mzc.lms.organization.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    private Long id;
    private String departmentCode;
    private String departmentName;
    private Long collegeId;
    private String collegeName;
    private LocalDateTime createdAt;

    public void updateName(String newName) {
        this.departmentName = newName;
    }

    public void updateCode(String newCode) {
        this.departmentCode = newCode;
    }

    public void changeCollege(Long newCollegeId) {
        this.collegeId = newCollegeId;
    }
}

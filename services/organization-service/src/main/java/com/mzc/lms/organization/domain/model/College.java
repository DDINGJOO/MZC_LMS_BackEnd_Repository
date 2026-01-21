package com.mzc.lms.organization.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class College {

    private Long id;
    private String collegeCode;
    private String collegeNumberCode;
    private String collegeName;
    private LocalDateTime createdAt;

    @Builder.Default
    private List<Department> departments = new ArrayList<>();

    public void addDepartment(Department department) {
        this.departments.add(department);
    }

    public void updateName(String newName) {
        this.collegeName = newName;
    }

    public void updateCode(String newCode) {
        this.collegeCode = newCode;
    }
}

package com.mzc.lms.profile.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfile {

    private Long studentId;
    private Long userId;
    private Integer admissionYear;
    private Integer grade;
    private String departmentName;
    private String collegeName;
    private LocalDateTime createdAt;

    public void updateGrade(Integer newGrade) {
        if (newGrade < 1 || newGrade > 4) {
            throw new IllegalArgumentException("Grade must be between 1 and 4");
        }
        this.grade = newGrade;
    }

    public String getFormattedStudentId() {
        return String.format("%d", studentId);
    }
}

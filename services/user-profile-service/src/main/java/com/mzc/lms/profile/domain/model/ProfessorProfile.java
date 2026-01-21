package com.mzc.lms.profile.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorProfile {

    private Long professorId;
    private Long userId;
    private LocalDate appointmentDate;
    private String departmentName;
    private String collegeName;
    private LocalDateTime createdAt;

    public String getFormattedProfessorId() {
        return String.format("%d", professorId);
    }

    public int getYearsOfService() {
        if (appointmentDate == null) {
            return 0;
        }
        return LocalDate.now().getYear() - appointmentDate.getYear();
    }
}

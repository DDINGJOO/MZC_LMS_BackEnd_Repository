package com.mzc.lms.organization.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicTerm {

    private Long id;
    private Integer year;
    private String termType;
    private LocalDate startDate;
    private LocalDate endDate;

    public boolean isCurrent() {
        LocalDate now = LocalDate.now();
        return !now.isBefore(startDate) && !now.isAfter(endDate);
    }

    public boolean isPast() {
        return LocalDate.now().isAfter(endDate);
    }

    public boolean isFuture() {
        return LocalDate.now().isBefore(startDate);
    }

    public String getTermName() {
        return year + "년 " + getTermTypeKorean();
    }

    private String getTermTypeKorean() {
        return switch (termType) {
            case "SPRING" -> "1학기";
            case "SUMMER" -> "여름학기";
            case "FALL" -> "2학기";
            case "WINTER" -> "겨울학기";
            default -> termType;
        };
    }

    public void updatePeriod(LocalDate newStartDate, LocalDate newEndDate) {
        if (newEndDate.isBefore(newStartDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        this.startDate = newStartDate;
        this.endDate = newEndDate;
    }
}

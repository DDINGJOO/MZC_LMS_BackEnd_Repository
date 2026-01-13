package com.mzc.backend.lms.domains.academy.adapter.in.web.dto;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 학기 응답 DTO
 */
@Getter
@Builder
public class AcademicTermResponseDto {

    private Long id;
    private Integer year;
    private String termType;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Entity -> DTO 변환
     */
    public static AcademicTermResponseDto from(AcademicTerm academicTerm) {
        return AcademicTermResponseDto.builder()
                .id(academicTerm.getId())
                .year(academicTerm.getYear())
                .termType(academicTerm.getTermType())
                .startDate(academicTerm.getStartDate())
                .endDate(academicTerm.getEndDate())
                .build();
    }
}

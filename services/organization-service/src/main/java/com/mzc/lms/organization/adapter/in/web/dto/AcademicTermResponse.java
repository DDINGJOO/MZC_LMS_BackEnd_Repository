package com.mzc.lms.organization.adapter.in.web.dto;

import com.mzc.lms.organization.domain.model.AcademicTerm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicTermResponse {

    private Long id;
    private Integer year;
    private String termType;
    private String termName;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean current;
    private boolean past;
    private boolean future;

    public static AcademicTermResponse from(AcademicTerm term) {
        return AcademicTermResponse.builder()
                .id(term.getId())
                .year(term.getYear())
                .termType(term.getTermType())
                .termName(term.getTermName())
                .startDate(term.getStartDate())
                .endDate(term.getEndDate())
                .current(term.isCurrent())
                .past(term.isPast())
                .future(term.isFuture())
                .build();
    }

    public static List<AcademicTermResponse> from(List<AcademicTerm> terms) {
        return terms.stream()
                .map(AcademicTermResponse::from)
                .toList();
    }
}

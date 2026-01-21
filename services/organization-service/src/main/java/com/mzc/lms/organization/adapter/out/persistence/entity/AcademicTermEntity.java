package com.mzc.lms.organization.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "academic_terms", indexes = {
    @Index(name = "idx_academic_terms_year", columnList = "year"),
    @Index(name = "idx_academic_terms_dates", columnList = "start_date, end_date")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AcademicTermEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "term_type", length = 20, nullable = false)
    private String termType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Builder
    public AcademicTermEntity(Integer year, String termType, LocalDate startDate, LocalDate endDate) {
        this.year = year;
        this.termType = termType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void updatePeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

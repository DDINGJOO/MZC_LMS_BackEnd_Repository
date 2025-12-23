package com.mzc.backend.lms.domains.academy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/*
    AcademicTerm 엔티티
    academic_terms 테이블과 매핑
*/

@Entity
@Table(name = "academic_terms")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicTerm {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 학기 식별자
	
	@Column(name = "year", nullable = false)
	private Integer year; // 학년도
	
	@Column(name = "term_type", nullable = false)
	private String termType; // 학기 구분 (1:봄학기/2:가을학기/SUMMER:여름학기/WINTER:겨울학기)
	
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate; // 학기 시작일
	
	@Column(name = "end_date", nullable = false)
	private LocalDate endDate; // 학기 종료일
}

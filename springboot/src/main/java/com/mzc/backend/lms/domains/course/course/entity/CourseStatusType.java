package com.mzc.backend.lms.domains.course.course.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
    CourseStatusType 엔티티
    course_status_types 테이블과 매핑
*/

@Entity
@Table(name = "course_status_types")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseStatusType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "status_code", length = 20, unique = true, nullable = false)
	private String statusCode; // 상태 코드 (ONGOING/COMPLETED/PENDING)"
	
	@Column(name = "status_name", length = 50, nullable = false)
	private String statusName; // 상태 이름 (진행중/완료/대기)
}

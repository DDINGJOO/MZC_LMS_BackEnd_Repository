package com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * 강의 검색 요청 DTO
 */
@Getter
@Builder
public class CourseSearchRequestDto {

    @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다")
    private Integer page;

    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
    private Integer size;

    private String keyword;

    private Long departmentId;

    private Integer courseType; // MAJOR_REQ, MAJOR_ELEC, GEN_REQ, GEN_ELEC

    private Integer credits;

    @NotNull(message = "수강신청 기간 ID는 필수입니다")
    private Long enrollmentPeriodId; // 필수 (enrollment_periods.id)

    private String sort; // 기본값: courseCode,asc
}

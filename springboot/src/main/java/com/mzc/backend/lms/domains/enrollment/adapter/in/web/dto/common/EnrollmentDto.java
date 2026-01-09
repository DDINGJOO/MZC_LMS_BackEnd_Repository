package com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.common;

import lombok.Builder;
import lombok.Getter;

/**
 * 수강신청 정보 DTO
 */
@Getter
@Builder
public class EnrollmentDto {
    private Integer current;
    private Integer max;
    private Boolean isFull;
}

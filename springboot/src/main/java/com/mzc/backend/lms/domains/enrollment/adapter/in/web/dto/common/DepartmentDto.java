package com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.common;

import lombok.Builder;
import lombok.Getter;

/**
 * 학과 정보 DTO
 */
@Getter
@Builder
public class DepartmentDto {
    private Long id;
    private String name;
}

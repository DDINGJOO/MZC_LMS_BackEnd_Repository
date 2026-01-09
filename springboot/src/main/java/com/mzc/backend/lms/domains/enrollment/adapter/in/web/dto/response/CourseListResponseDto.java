package com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response;

import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.common.CourseItemDto;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

/**
 * 강의 목록 응답 DTO
 */
@Getter
@Builder
public class CourseListResponseDto {
    private List<CourseItemDto> content;
    private int totalElements;
    private int totalPages;
    private int currentPage;
    private int size;
}

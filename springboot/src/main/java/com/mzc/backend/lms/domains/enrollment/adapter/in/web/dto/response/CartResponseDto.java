package com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response;

import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.common.CartItemDto;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

/**
 * 장바구니 조회 응답 DTO
 */
@Getter
@Builder
public class CartResponseDto {
    private Integer totalCourses;
    private Integer totalCredits;
    private List<CartItemDto> courses;
}

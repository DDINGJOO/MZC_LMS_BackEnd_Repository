package com.mzc.backend.lms.domains.enrollment.application.port.in;

import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request.CartBulkDeleteRequestDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request.CourseIdsRequestDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.CartBulkAddResponseDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.CartBulkDeleteResponseDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.CartResponseDto;

/**
 * 장바구니 UseCase (Inbound Port)
 */
public interface CartUseCase {

    /**
     * 학생의 장바구니 조회
     */
    CartResponseDto getCart(String studentId);

    /**
     * 장바구니에 강의 일괄 추가
     */
    CartBulkAddResponseDto addToCartBulk(CourseIdsRequestDto request, String studentId);

    /**
     * 장바구니에서 강의 일괄 삭제
     */
    CartBulkDeleteResponseDto deleteFromCartBulk(CartBulkDeleteRequestDto request, String studentId);

    /**
     * 장바구니 전체 비우기
     */
    CartBulkDeleteResponseDto deleteAllCart(String studentId);
}

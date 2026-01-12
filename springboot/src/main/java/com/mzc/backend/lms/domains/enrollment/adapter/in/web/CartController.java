package com.mzc.backend.lms.domains.enrollment.adapter.in.web;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request.CourseIdsRequestDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.CartBulkAddResponseDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request.CartBulkDeleteRequestDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.CartBulkDeleteResponseDto;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.CartResponseDto;
import com.mzc.backend.lms.domains.enrollment.application.port.in.CartUseCase;
import com.mzc.backend.lms.domains.user.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 장바구니 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartUseCase cartUseCase;

    /**
     * 장바구니 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> getCart(
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("장바구니 조회: studentId={}", studentId);

        CartResponseDto response = cartUseCase.getCart(String.valueOf(studentId));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 장바구니에 강의 요청 항목 일괄 추가
     */
    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<CartBulkAddResponseDto>> addToCartBulk(
            @RequestBody CourseIdsRequestDto request,
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("장바구니 요청 항목 일괄 추가: studentId={}, courseIds={}", studentId, request.getCourseIds());

        CartBulkAddResponseDto response = cartUseCase.addToCartBulk(request, String.valueOf(studentId));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 장바구니에서 강의 요청 항목 일괄 삭제
     */
    @DeleteMapping("/bulk")
    public ResponseEntity<ApiResponse<CartBulkDeleteResponseDto>> deleteFromCartBulk(
            @RequestBody CartBulkDeleteRequestDto request,
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("장바구니 요청 항목 일괄 삭제: studentId={}, cartIds={}", studentId, request.getCartIds());

        CartBulkDeleteResponseDto response = cartUseCase.deleteFromCartBulk(request, String.valueOf(studentId));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 장바구니 전체 비우기
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<CartBulkDeleteResponseDto>> deleteAllCart(
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }

        log.debug("장바구니 전체 비우기: studentId={}", studentId);

        CartBulkDeleteResponseDto response = cartUseCase.deleteAllCart(String.valueOf(studentId));
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

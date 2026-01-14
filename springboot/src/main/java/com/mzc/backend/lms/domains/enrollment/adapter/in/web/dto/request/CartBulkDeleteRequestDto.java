package com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 장바구니 일괄 삭제 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartBulkDeleteRequestDto {

    @NotEmpty(message = "삭제할 장바구니 ID 목록은 필수입니다")
    private List<Long> cartIds;
}

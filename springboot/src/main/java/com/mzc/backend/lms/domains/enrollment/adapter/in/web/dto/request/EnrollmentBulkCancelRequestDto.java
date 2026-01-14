package com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 수강신청 일괄 취소 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentBulkCancelRequestDto {

    @NotEmpty(message = "취소할 수강신청 ID 목록은 필수입니다")
    private List<Long> enrollmentIds;
}

package com.mzc.lms.enrollment.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawEnrollmentRequest {

    @NotBlank(message = "Withdrawal reason is required")
    @Size(max = 500, message = "Withdrawal reason must not exceed 500 characters")
    private String reason;
}

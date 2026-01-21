package com.mzc.lms.progress.adapter.in.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateContentProgressRequest {

    @NotNull(message = "Content ID is required")
    private Long contentId;

    @NotBlank(message = "Content type is required")
    private String contentType;

    @NotNull(message = "Total time is required")
    @Min(value = 1, message = "Total time must be at least 1 second")
    private Long totalTimeSeconds;
}

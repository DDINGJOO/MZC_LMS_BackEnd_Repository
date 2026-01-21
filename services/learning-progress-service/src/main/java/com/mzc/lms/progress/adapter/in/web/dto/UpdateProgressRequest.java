package com.mzc.lms.progress.adapter.in.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProgressRequest {

    @NotNull(message = "Completed contents is required")
    @Min(value = 0, message = "Completed contents must be at least 0")
    private Integer completedContents;

    @Min(value = 0, message = "Additional time must be at least 0")
    private Long additionalTimeSeconds;
}

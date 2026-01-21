package com.mzc.lms.catalog.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PrerequisiteRequest {

    @NotNull(message = "Prerequisite subject ID is required")
    private Long prerequisiteId;

    private Boolean isMandatory = true;
}

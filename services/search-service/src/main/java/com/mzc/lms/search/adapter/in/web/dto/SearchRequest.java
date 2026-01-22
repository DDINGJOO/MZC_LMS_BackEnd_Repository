package com.mzc.lms.search.adapter.in.web.dto;

import com.mzc.lms.search.domain.model.SearchableType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    @NotBlank(message = "Query is required")
    private String query;

    private SearchableType type;

    @Min(value = 0, message = "Page must be greater than or equal to 0")
    @Builder.Default
    private int page = 0;

    @Min(value = 1, message = "Size must be greater than 0")
    @Builder.Default
    private int size = 20;
}

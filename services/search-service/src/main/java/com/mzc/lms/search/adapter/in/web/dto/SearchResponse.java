package com.mzc.lms.search.adapter.in.web.dto;

import com.mzc.lms.search.domain.model.SearchDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private List<SearchDocument> documents;
    private long totalHits;
    private int currentPage;
    private int pageSize;
    private Map<String, Map<String, Long>> aggregations;
}

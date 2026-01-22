package com.mzc.lms.search.domain.model;

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
public class SearchResult {
    private List<SearchDocument> documents;
    private long totalHits;
    private Map<String, Map<String, Long>> aggregations;
}

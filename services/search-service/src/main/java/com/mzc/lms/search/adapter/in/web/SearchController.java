package com.mzc.lms.search.adapter.in.web;

import com.mzc.lms.search.adapter.in.web.dto.SearchRequest;
import com.mzc.lms.search.adapter.in.web.dto.SearchResponse;
import com.mzc.lms.search.adapter.in.web.dto.SuggestionResponse;
import com.mzc.lms.search.application.port.in.SearchUseCase;
import com.mzc.lms.search.domain.model.SearchResult;
import com.mzc.lms.search.domain.model.SearchableType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Search", description = "Search API")
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchUseCase searchUseCase;

    @Operation(summary = "Search documents", description = "Search for documents using Elasticsearch")
    @GetMapping
    public ResponseEntity<SearchResponse> search(
            @RequestParam String query,
            @RequestParam(required = false) SearchableType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        SearchResult result = searchUseCase.search(query, type, page, size);

        SearchResponse response = SearchResponse.builder()
                .documents(result.getDocuments())
                .totalHits(result.getTotalHits())
                .currentPage(page)
                .pageSize(size)
                .aggregations(result.getAggregations())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get search suggestions", description = "Get autocomplete suggestions for search")
    @GetMapping("/suggest")
    public ResponseEntity<SuggestionResponse> suggest(
            @RequestParam String query,
            @RequestParam(required = false) SearchableType type) {

        List<String> suggestions = searchUseCase.suggest(query, type);

        SuggestionResponse response = SuggestionResponse.builder()
                .suggestions(suggestions)
                .build();

        return ResponseEntity.ok(response);
    }
}

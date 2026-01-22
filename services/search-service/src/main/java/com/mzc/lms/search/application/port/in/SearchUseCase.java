package com.mzc.lms.search.application.port.in;

import com.mzc.lms.search.domain.model.SearchDocument;
import com.mzc.lms.search.domain.model.SearchResult;
import com.mzc.lms.search.domain.model.SearchableType;

import java.util.List;

public interface SearchUseCase {
    SearchResult search(String query, SearchableType type, int page, int size);

    List<String> suggest(String query, SearchableType type);

    void indexDocument(SearchDocument document);

    void deleteDocument(String id, SearchableType type);
}

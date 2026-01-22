package com.mzc.lms.search.application.port.out;

import com.mzc.lms.search.domain.model.SearchDocument;
import com.mzc.lms.search.domain.model.SearchResult;
import com.mzc.lms.search.domain.model.SearchableType;

import java.util.List;

public interface SearchRepository {
    SearchResult search(String query, SearchableType type, int page, int size);

    List<String> suggest(String query, SearchableType type);

    void save(SearchDocument document);

    void delete(String id, SearchableType type);
}

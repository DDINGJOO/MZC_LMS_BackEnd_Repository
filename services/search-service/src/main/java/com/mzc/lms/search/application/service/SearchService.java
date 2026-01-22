package com.mzc.lms.search.application.service;

import com.mzc.lms.search.application.port.in.SearchUseCase;
import com.mzc.lms.search.application.port.out.SearchRepository;
import com.mzc.lms.search.domain.model.SearchDocument;
import com.mzc.lms.search.domain.model.SearchResult;
import com.mzc.lms.search.domain.model.SearchableType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService implements SearchUseCase {

    private final SearchRepository searchRepository;

    @Override
    public SearchResult search(String query, SearchableType type, int page, int size) {
        log.info("Searching for query: {}, type: {}, page: {}, size: {}", query, type, page, size);
        return searchRepository.search(query, type, page, size);
    }

    @Override
    public List<String> suggest(String query, SearchableType type) {
        log.info("Getting suggestions for query: {}, type: {}", query, type);
        return searchRepository.suggest(query, type);
    }

    @Override
    public void indexDocument(SearchDocument document) {
        log.info("Indexing document: id={}, type={}", document.getId(), document.getType());
        searchRepository.save(document);
    }

    @Override
    public void deleteDocument(String id, SearchableType type) {
        log.info("Deleting document: id={}, type={}", id, type);
        searchRepository.delete(id, type);
    }
}

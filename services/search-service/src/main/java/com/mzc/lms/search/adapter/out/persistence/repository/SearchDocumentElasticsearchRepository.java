package com.mzc.lms.search.adapter.out.persistence.repository;

import com.mzc.lms.search.adapter.out.persistence.entity.SearchDocumentEntity;
import com.mzc.lms.search.domain.model.SearchableType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SearchDocumentElasticsearchRepository extends ElasticsearchRepository<SearchDocumentEntity, String> {
    List<SearchDocumentEntity> findByType(SearchableType type);

    void deleteByIdAndType(String id, SearchableType type);
}

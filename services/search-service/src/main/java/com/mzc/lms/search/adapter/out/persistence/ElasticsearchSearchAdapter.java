package com.mzc.lms.search.adapter.out.persistence;

import com.mzc.lms.search.adapter.out.persistence.entity.SearchDocumentEntity;
import com.mzc.lms.search.adapter.out.persistence.repository.SearchDocumentElasticsearchRepository;
import com.mzc.lms.search.application.port.out.SearchRepository;
import com.mzc.lms.search.domain.model.SearchDocument;
import com.mzc.lms.search.domain.model.SearchResult;
import com.mzc.lms.search.domain.model.SearchableType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticsearchSearchAdapter implements SearchRepository {

    private final SearchDocumentElasticsearchRepository elasticsearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public SearchResult search(String query, SearchableType type, int page, int size) {
        try {
            NativeQuery searchQuery = NativeQuery.builder()
                    .withQuery(q -> q
                            .bool(b -> {
                                b.must(m -> m
                                        .multiMatch(mm -> mm
                                                .query(query)
                                                .fields("title^3", "content^2", "tags")
                                        )
                                );
                                if (type != null) {
                                    b.filter(f -> f
                                            .term(t -> t
                                                    .field("type")
                                                    .value(type.name())
                                            )
                                    );
                                }
                                return b;
                            })
                    )
                    .withPageable(PageRequest.of(page, size))
                    .build();

            SearchHits<SearchDocumentEntity> searchHits = elasticsearchOperations.search(
                    searchQuery,
                    SearchDocumentEntity.class,
                    IndexCoordinates.of("search-documents")
            );

            List<SearchDocument> documents = searchHits.getSearchHits().stream()
                    .map(SearchHit::getContent)
                    .map(this::toModel)
                    .collect(Collectors.toList());

            return SearchResult.builder()
                    .documents(documents)
                    .totalHits(searchHits.getTotalHits())
                    .aggregations(new HashMap<>())
                    .build();
        } catch (Exception e) {
            log.error("Error searching documents", e);
            return SearchResult.builder()
                    .documents(new ArrayList<>())
                    .totalHits(0)
                    .aggregations(new HashMap<>())
                    .build();
        }
    }

    @Override
    public List<String> suggest(String query, SearchableType type) {
        try {
            NativeQuery searchQuery = NativeQuery.builder()
                    .withQuery(q -> q
                            .bool(b -> {
                                b.must(m -> m
                                        .matchPhrase(mp -> mp
                                                .field("title")
                                                .query(query)
                                        )
                                );
                                if (type != null) {
                                    b.filter(f -> f
                                            .term(t -> t
                                                    .field("type")
                                                    .value(type.name())
                                            )
                                    );
                                }
                                return b;
                            })
                    )
                    .withPageable(PageRequest.of(0, 10))
                    .build();

            SearchHits<SearchDocumentEntity> searchHits = elasticsearchOperations.search(
                    searchQuery,
                    SearchDocumentEntity.class,
                    IndexCoordinates.of("search-documents")
            );

            return searchHits.getSearchHits().stream()
                    .map(SearchHit::getContent)
                    .map(SearchDocumentEntity::getTitle)
                    .distinct()
                    .limit(10)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting suggestions", e);
            return new ArrayList<>();
        }
    }

    @Override
    public void save(SearchDocument document) {
        SearchDocumentEntity entity = toEntity(document);
        elasticsearchRepository.save(entity);
        log.info("Document saved to Elasticsearch: id={}", document.getId());
    }

    @Override
    public void delete(String id, SearchableType type) {
        elasticsearchRepository.deleteByIdAndType(id, type);
        log.info("Document deleted from Elasticsearch: id={}, type={}", id, type);
    }

    private SearchDocument toModel(SearchDocumentEntity entity) {
        return SearchDocument.builder()
                .id(entity.getId())
                .type(entity.getType())
                .title(entity.getTitle())
                .content(entity.getContent())
                .tags(entity.getTags())
                .metadata(entity.getMetadata())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private SearchDocumentEntity toEntity(SearchDocument document) {
        return SearchDocumentEntity.builder()
                .id(document.getId())
                .type(document.getType())
                .title(document.getTitle())
                .content(document.getContent())
                .tags(document.getTags())
                .metadata(document.getMetadata())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
}

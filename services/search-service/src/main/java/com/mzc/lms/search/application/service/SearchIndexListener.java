package com.mzc.lms.search.application.service;

import com.mzc.lms.search.application.port.in.SearchUseCase;
import com.mzc.lms.search.domain.event.SearchEvent;
import com.mzc.lms.search.domain.model.SearchDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchIndexListener {

    private final SearchUseCase searchUseCase;

    @KafkaListener(topics = "course-events", groupId = "search-service-group")
    public void handleCourseEvent(SearchEvent event) {
        log.info("Received course event: {}", event.getEventId());
        processEvent(event);
    }

    @KafkaListener(topics = "user-events", groupId = "search-service-group")
    public void handleUserEvent(SearchEvent event) {
        log.info("Received user event: {}", event.getEventId());
        processEvent(event);
    }

    @KafkaListener(topics = "post-events", groupId = "search-service-group")
    public void handlePostEvent(SearchEvent event) {
        log.info("Received post event: {}", event.getEventId());
        processEvent(event);
    }

    private void processEvent(SearchEvent event) {
        try {
            switch (event.getEventType()) {
                case CREATED, UPDATED -> {
                    SearchDocument document = SearchDocument.builder()
                            .id(event.getEntityId())
                            .type(event.getType())
                            .title(event.getTitle())
                            .content(event.getContent())
                            .tags(event.getTags())
                            .metadata(event.getMetadata())
                            .createdAt(event.getTimestamp())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    searchUseCase.indexDocument(document);
                    log.info("Successfully indexed document: id={}, type={}", event.getEntityId(), event.getType());
                }
                case DELETED -> {
                    searchUseCase.deleteDocument(event.getEntityId(), event.getType());
                    log.info("Successfully deleted document: id={}, type={}", event.getEntityId(), event.getType());
                }
            }
        } catch (Exception e) {
            log.error("Error processing search event: {}", event.getEventId(), e);
        }
    }
}

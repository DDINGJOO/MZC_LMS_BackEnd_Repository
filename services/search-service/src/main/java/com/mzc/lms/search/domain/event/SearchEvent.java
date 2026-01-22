package com.mzc.lms.search.domain.event;

import com.mzc.lms.search.domain.model.SearchableType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchEvent {
    private String eventId;
    private String entityId;
    private SearchableType type;
    private EventType eventType;
    private String title;
    private String content;
    private List<String> tags;
    private Map<String, Object> metadata;
    private LocalDateTime timestamp;

    public enum EventType {
        CREATED,
        UPDATED,
        DELETED
    }
}

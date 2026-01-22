package com.mzc.lms.search.adapter.out.kafka;

import com.mzc.lms.search.application.port.out.SearchEventPublisher;
import com.mzc.lms.search.domain.event.SearchEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchKafkaEventPublisher implements SearchEventPublisher {

    private final KafkaTemplate<String, SearchEvent> kafkaTemplate;

    @Override
    public void publish(SearchEvent event) {
        String topic = "search-events";
        kafkaTemplate.send(topic, event.getEntityId(), event);
        log.info("Published search event to {}: eventId={}", topic, event.getEventId());
    }
}

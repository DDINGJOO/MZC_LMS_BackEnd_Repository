package com.mzc.lms.search.application.port.out;

import com.mzc.lms.search.domain.event.SearchEvent;

public interface SearchEventPublisher {
    void publish(SearchEvent event);
}

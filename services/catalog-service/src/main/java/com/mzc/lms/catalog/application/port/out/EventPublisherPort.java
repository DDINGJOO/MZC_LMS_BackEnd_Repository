package com.mzc.lms.catalog.application.port.out;

import com.mzc.lms.catalog.domain.event.CatalogEvent;

public interface EventPublisherPort {

    void publish(CatalogEvent event);
}

package com.mzc.lms.catalog.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String CATALOG_EVENTS_TOPIC = "catalog-events";

    @Bean
    public NewTopic catalogEventsTopic() {
        return TopicBuilder.name(CATALOG_EVENTS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}

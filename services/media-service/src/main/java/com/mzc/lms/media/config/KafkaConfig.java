package com.mzc.lms.media.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topics.media-events:media-events}")
    private String mediaEventsTopic;

    @Value("${kafka.topics.partitions:3}")
    private int partitions;

    @Value("${kafka.topics.replicas:1}")
    private int replicas;

    @Bean
    public NewTopic mediaEventsTopic() {
        return TopicBuilder.name(mediaEventsTopic)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }
}

package com.mzc.lms.course.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String COURSE_EVENTS_TOPIC = "course-events";

    @Bean
    public NewTopic courseEventsTopic() {
        return TopicBuilder.name(COURSE_EVENTS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}

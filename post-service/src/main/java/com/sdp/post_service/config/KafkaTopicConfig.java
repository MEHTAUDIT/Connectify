package com.sdp.post_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic postCreatedTopic() {
        return TopicBuilder.name("post-created-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic postDeletedTopic() {
        return TopicBuilder.name("post-deleted-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic postUpdatedTopic() {
        return TopicBuilder.name("post-updated-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic postLikedTopic() {
        return TopicBuilder.name("post-liked-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }
}

package org.example.corporatebankingsystem.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic userEventsTopic() {
        return TopicBuilder.name("user-events")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic clientEventsTopic() {
        return TopicBuilder.name("client-events")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic creditRequestEventsTopic() {
        return TopicBuilder.name("credit-request-events")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic requestReviewEventsTopic() {
        return TopicBuilder.name("request-review-events")
                .partitions(1)
                .replicas(1)
                .build();
    }
}

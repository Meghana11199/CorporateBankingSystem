package org.example.corporatebankingsystem;

import org.apache.kafka.clients.admin.NewTopic;
import org.example.corporatebankingsystem.Config.KafkaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class KafkaConfigTest {

    @Test
    void userEventsTopicBean_shouldBeCreatedCorrectly() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(KafkaConfig.class)) {

            NewTopic topic = context.getBean("userEventsTopic", NewTopic.class);

            assertNotNull(topic);
            assertEquals("user-events", topic.name());
            assertEquals(1, topic.numPartitions());
            assertEquals(1, topic.replicationFactor());
        }
    }

    @Test
    void clientEventsTopicBean_shouldBeCreatedCorrectly() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(KafkaConfig.class)) {

            NewTopic topic = context.getBean("clientEventsTopic", NewTopic.class);

            assertNotNull(topic);
            assertEquals("client-events", topic.name());
            assertEquals(1, topic.numPartitions());
            assertEquals(1, topic.replicationFactor());
        }
    }

    @Test
    void creditRequestEventsTopicBean_shouldBeCreatedCorrectly() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(KafkaConfig.class)) {

            NewTopic topic = context.getBean("creditRequestEventsTopic", NewTopic.class);

            assertNotNull(topic);
            assertEquals("credit-request-events", topic.name());
            assertEquals(1, topic.numPartitions());
            assertEquals(1, topic.replicationFactor());
        }
    }

    @Test
    void requestReviewEventsTopicBean_shouldBeCreatedCorrectly() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(KafkaConfig.class)) {

            NewTopic topic = context.getBean("requestReviewEventsTopic", NewTopic.class);

            assertNotNull(topic);
            assertEquals("request-review-events", topic.name());
            assertEquals(1, topic.numPartitions());
            assertEquals(1, topic.replicationFactor());
        }
    }
}
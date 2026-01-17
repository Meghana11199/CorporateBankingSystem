package org.example.corporatebankingsystem.Kafkatest;

import org.example.corporatebankingsystem.event.UserEvent;
import org.example.corporatebankingsystem.kafka.UserEventProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

class UserEventProducerTest {

    @Mock
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @InjectMocks
    private UserEventProducer producer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendUserEvent_shouldPublishUserEventToKafka() {
        // Arrange
        UserEvent event = new UserEvent(
                "USER_CREATED",
                "USER_101",
                "alice.wonder",
                "ADMIN"
        );

        // Act
        producer.sendUserEvent(event);

        // Assert
        verify(kafkaTemplate).send(
                "user-events",
                "USER_101",
                event
        );
    }
}
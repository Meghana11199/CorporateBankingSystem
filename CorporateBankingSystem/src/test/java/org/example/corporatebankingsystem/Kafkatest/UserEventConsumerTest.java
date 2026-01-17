package org.example.corporatebankingsystem.Kafkatest;

import org.example.corporatebankingsystem.event.UserEvent;
import org.example.corporatebankingsystem.kafka.UserEventConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class UserEventConsumerTest {

    private UserEventConsumer consumer;

    @BeforeEach
    void setUp() {
        consumer = new UserEventConsumer();
    }

    @Test
    void consume_shouldHandleUserCreatedEvent() {
        // Arrange
        UserEvent event = new UserEvent(
                "USER_CREATED",
                "USER_001",
                "john.doe",
                "ADMIN"
        );

        // Act & Assert
        assertDoesNotThrow(() -> consumer.consume(event));
    }

    @Test
    void consume_shouldHandleUserLoginEvent() {
        // Arrange
        UserEvent event = new UserEvent(
                "USER_LOGIN",
                "USER_002",
                "jane.smith",
                "RM"
        );

        // Act & Assert
        assertDoesNotThrow(() -> consumer.consume(event));
    }

    @Test
    void consume_shouldHandleUserDisabledEvent() {
        // Arrange
        UserEvent event = new UserEvent(
                "USER_DISABLED",
                "USER_003",
                "mark.taylor",
                "ANALYST"
        );

        // Act & Assert
        assertDoesNotThrow(() -> consumer.consume(event));
    }
}

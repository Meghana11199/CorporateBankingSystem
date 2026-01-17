package org.example.corporatebankingsystem.Kafkatest;

import org.example.corporatebankingsystem.event.ClientEvent;
import org.example.corporatebankingsystem.kafka.ClientEventConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ClientEventConsumerTest {

    private ClientEventConsumer consumer;

    @BeforeEach
    void setUp() {
        consumer = new ClientEventConsumer();
    }

    @Test
    void consume_shouldHandleClientCreatedEvent() {
        // Arrange
        ClientEvent event = new ClientEvent(
                "CLIENT_CREATED",
                "CLIENT_001",
                "Acme Corporation",
                "RM_1001"
        );

        // Act & Assert
        assertDoesNotThrow(() -> consumer.consume(event));
    }

    @Test
    void consume_shouldHandleClientUpdatedEvent() {
        // Arrange
        ClientEvent event = new ClientEvent(
                "CLIENT_UPDATED",
                "CLIENT_002",
                "Global Tech Ltd",
                "RM_2002"
        );

        // Act & Assert
        assertDoesNotThrow(() -> consumer.consume(event));
    }
}
package org.example.corporatebankingsystem.Kafkatest;

import org.example.corporatebankingsystem.event.CreditRequestEvent;
import org.example.corporatebankingsystem.kafka.CreditRequestEventConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CreditRequestEventConsumerTest {

    private CreditRequestEventConsumer consumer;

    @BeforeEach
    void setUp() {
        consumer = new CreditRequestEventConsumer();
    }

    @Test
    void consume_shouldHandleCreditRequestCreatedEvent() {
        // Arrange
        CreditRequestEvent event = new CreditRequestEvent(
                "CREDIT_REQUEST_CREATED",
                "CR_001",
                "CLIENT_101",
                "RM_5001",
                "PENDING",
                250000.00
        );

        // Act & Assert
        assertDoesNotThrow(() -> consumer.consume(event));
    }

    @Test
    void consume_shouldHandleCreditRequestApprovedEvent() {
        // Arrange
        CreditRequestEvent event = new CreditRequestEvent(
                "CREDIT_REQUEST_APPROVED",
                "CR_002",
                "CLIENT_202",
                "RM_6002",
                "APPROVED",
                500000.00
        );

        // Act & Assert
        assertDoesNotThrow(() -> consumer.consume(event));
    }
}
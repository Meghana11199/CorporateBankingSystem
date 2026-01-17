package org.example.corporatebankingsystem.Kafkatest;

import org.example.corporatebankingsystem.event.RequestReviewEvent;
import org.example.corporatebankingsystem.kafka.RequestReviewEventConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class RequestReviewEventConsumerTest {

    private RequestReviewEventConsumer consumer;

    @BeforeEach
    void setUp() {
        consumer = new RequestReviewEventConsumer();
    }

    @Test
    void consume_shouldHandleRequestReviewedEvent() {
        // Arrange
        RequestReviewEvent event = new RequestReviewEvent(
                "REQUEST_REVIEWED",
                "REQ_1001",
                "ANALYST_01",
                "IN_REVIEW",
                "Initial review started"
        );

        // Act & Assert
        assertDoesNotThrow(() -> consumer.consume(event));
    }

    @Test
    void consume_shouldHandleRequestApprovedEvent() {
        // Arrange
        RequestReviewEvent event = new RequestReviewEvent(
                "REQUEST_APPROVED",
                "REQ_1002",
                "ANALYST_02",
                "APPROVED",
                "Approved after risk assessment"
        );

        // Act & Assert
        assertDoesNotThrow(() -> consumer.consume(event));
    }

    @Test
    void consume_shouldHandleRequestRejectedEvent() {
        // Arrange
        RequestReviewEvent event = new RequestReviewEvent(
                "REQUEST_REJECTED",
                "REQ_1003",
                "ANALYST_03",
                "REJECTED",
                "Insufficient credit history"
        );

        // Act & Assert
        assertDoesNotThrow(() -> consumer.consume(event));
    }
}
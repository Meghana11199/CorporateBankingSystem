package org.example.corporatebankingsystem.Kafkatest;

import org.example.corporatebankingsystem.event.RequestReviewEvent;
import org.example.corporatebankingsystem.kafka.RequestReviewEventProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

class RequestReviewEventProducerTest {

    @Mock
    private KafkaTemplate<String, RequestReviewEvent> kafkaTemplate;

    @InjectMocks
    private RequestReviewEventProducer producer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void send_shouldPublishRequestReviewEventToKafka() {
        // Arrange
        RequestReviewEvent event = new RequestReviewEvent(
                "REQUEST_APPROVED",
                "REQ_9001",
                "ANALYST_007",
                "APPROVED",
                "Approved after final review"
        );

        // Act
        producer.send(event);

        // Assert
        verify(kafkaTemplate).send(
                "request-review-events",
                "REQ_9001",
                event
        );
    }
}
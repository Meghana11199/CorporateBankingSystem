package org.example.corporatebankingsystem.Kafkatest;

import org.example.corporatebankingsystem.event.CreditRequestEvent;
import org.example.corporatebankingsystem.kafka.CreditRequestEventProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

class CreditRequestEventProducerTest {

    @Mock
    private KafkaTemplate<String, CreditRequestEvent> kafkaTemplate;

    @InjectMocks
    private CreditRequestEventProducer producer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendEvent_shouldSendCreditRequestEventToKafka() {
        // Arrange
        CreditRequestEvent event = new CreditRequestEvent(
                "CREDIT_REQUEST_CREATED",
                "CR_101",
                "CLIENT_501",
                "RM_9001",
                "PENDING",
                750000.00
        );

        // Act
        producer.sendEvent(event);

        // Assert
        verify(kafkaTemplate).send(
                "credit-request-events",
                "CR_101",
                event
        );
    }
}
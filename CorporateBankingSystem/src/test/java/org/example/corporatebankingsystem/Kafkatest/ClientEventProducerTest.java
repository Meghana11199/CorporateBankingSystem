package org.example.corporatebankingsystem.Kafkatest;

import org.example.corporatebankingsystem.event.ClientEvent;
import org.example.corporatebankingsystem.kafka.ClientEventProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

class ClientEventProducerTest {

    @Mock
    private KafkaTemplate<String, ClientEvent> kafkaTemplate;

    @InjectMocks
    private ClientEventProducer producer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendClientEvent_shouldSendEventToKafka() {
        // Arrange
        ClientEvent event = new ClientEvent(
                "CLIENT_CREATED",
                "CLIENT_001",
                "Acme Corporation",
                "RM_1001"
        );

        // Act
        producer.sendClientEvent(event);

        // Assert
        verify(kafkaTemplate).send(
                "client-events",
                "CLIENT_001",
                event
        );
    }
}
package org.example.corporatebankingsystem.kafka;

import lombok.RequiredArgsConstructor;
import org.example.corporatebankingsystem.event.ClientEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientEventProducer {

    private static final String TOPIC = "client-events";
    private final KafkaTemplate<String, ClientEvent> kafkaTemplate;

    public void sendClientEvent(ClientEvent event) {
        System.out.println("🚀 Sending Client Event: " + event);
        kafkaTemplate.send(TOPIC, event.getClientId(), event);
    }
}
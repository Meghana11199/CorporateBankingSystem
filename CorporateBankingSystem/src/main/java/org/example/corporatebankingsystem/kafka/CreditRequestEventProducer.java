package org.example.corporatebankingsystem.kafka;

import lombok.RequiredArgsConstructor;
import org.example.corporatebankingsystem.event.CreditRequestEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditRequestEventProducer {

    private final KafkaTemplate<String, CreditRequestEvent> kafkaTemplate;
    private static final String TOPIC = "credit-request-events";

    public void sendEvent(CreditRequestEvent event) {
        System.out.println("🚀 Sending Credit Request Event: " + event);
        kafkaTemplate.send(TOPIC, event.getCreditRequestId(), event);
    }
}
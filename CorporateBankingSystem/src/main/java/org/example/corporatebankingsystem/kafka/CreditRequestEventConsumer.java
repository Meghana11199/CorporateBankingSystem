package org.example.corporatebankingsystem.kafka;

import lombok.extern.slf4j.Slf4j;
import org.example.corporatebankingsystem.event.CreditRequestEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CreditRequestEventConsumer {

    @KafkaListener(
            topics = "credit-request-events",
            groupId = "audit-group"
    )
    public void consume(CreditRequestEvent event) {
        log.info("📩 Credit Request Event Received: {}", event);
        System.out.println("📩 Credit Request Event Received: " + event);
    }
}
package org.example.corporatebankingsystem.kafka;

import lombok.extern.slf4j.Slf4j;
import org.example.corporatebankingsystem.event.ClientEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientEventConsumer {

    @KafkaListener(
            topics = "client-events",
            groupId = "client-event-group"
    )
    public void consume(ClientEvent event) {
        log.info("📩 Client Event Received: {}", event);
        System.out.println("📩 Client Event Received: " + event);
    }
}
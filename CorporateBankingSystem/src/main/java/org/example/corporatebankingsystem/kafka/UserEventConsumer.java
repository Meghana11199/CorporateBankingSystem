package org.example.corporatebankingsystem.kafka;

import lombok.extern.slf4j.Slf4j;
import org.example.corporatebankingsystem.event.UserEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserEventConsumer {

    @KafkaListener(
            topics = "user-events",
            groupId = "user-event-group"
    )
    public void consume(UserEvent event) {
        log.info("📩 User Event Received: {}", event);
        System.out.println("📩 User Event Received: " + event);
    }
}

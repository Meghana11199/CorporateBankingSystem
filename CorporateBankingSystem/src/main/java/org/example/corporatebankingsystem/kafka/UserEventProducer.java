package org.example.corporatebankingsystem.kafka;

import lombok.RequiredArgsConstructor;
import org.example.corporatebankingsystem.event.UserEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private static final String TOPIC = "user-events";

    public void sendUserEvent(UserEvent event) {
        System.out.println("🚀 Sending User Event: " + event);
        kafkaTemplate.send(TOPIC, event.getUserId(), event);
    }
}
package org.example.corporatebankingsystem.kafka;

import lombok.RequiredArgsConstructor;
import org.example.corporatebankingsystem.event.RequestReviewEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestReviewEventProducer {

    private static final String TOPIC = "request-review-events";

    private final KafkaTemplate<String, RequestReviewEvent> kafkaTemplate;

    public void send(RequestReviewEvent event) {
        System.out.println("🚀 Sending RequestReviewEvent: " + event);
        kafkaTemplate.send(TOPIC, event.getRequestId(), event);
    }
}
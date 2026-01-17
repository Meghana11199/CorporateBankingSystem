package org.example.corporatebankingsystem.kafka;


import lombok.extern.slf4j.Slf4j;
import org.example.corporatebankingsystem.event.RequestReviewEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RequestReviewEventConsumer {

    @KafkaListener(
            topics = "request-review-events",
            groupId = "request-review-group"
    )
    public void consume(RequestReviewEvent event) {

        log.info("📩 Request Review Event Received: {}", event);
        System.out.println("📩 Request Review Event Received: " + event);

        // Future use cases:
        // - Save audit trail
        // - Notify RM
        // - Trigger email / SMS
    }
}

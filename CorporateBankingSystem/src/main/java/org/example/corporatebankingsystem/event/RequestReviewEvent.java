package org.example.corporatebankingsystem.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestReviewEvent {

    private String eventType;      // REQUEST_REVIEWED, REQUEST_APPROVED, REQUEST_REJECTED
    private String requestId;
    private String analystId;
    private String status;
    private String comment;
}
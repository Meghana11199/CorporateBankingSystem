package org.example.corporatebankingsystem.event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequestEvent {

    private String eventType;    // CREDIT_REQUEST_CREATED, UPDATED, APPROVED, REJECTED
    private String creditRequestId;
    private String clientId;
    private String rmId;
    private String status;
    private Double amount;
}
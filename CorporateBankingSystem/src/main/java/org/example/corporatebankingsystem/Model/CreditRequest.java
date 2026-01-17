package org.example.corporatebankingsystem.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "credit_requests")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditRequest {

    @Id
    private String id;

    private String clientId;
    private String rmId;

    private Double requestedAmount;
    private Integer tenureMonths;
    private String purpose;

    private CreditStatus status;// PENDING, APPROVED, REJECTED

    private String analystComment;


    private LocalDateTime createdAt;
}
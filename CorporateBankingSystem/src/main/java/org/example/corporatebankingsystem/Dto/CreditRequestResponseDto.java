package org.example.corporatebankingsystem.Dto;

import lombok.Builder;
import lombok.Data;
import org.example.corporatebankingsystem.Model.CreditStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class CreditRequestResponseDto {

    private String id;
    private String clientId;
    private String rmId;

    private Double requestedAmount;
    private Integer tenureMonths;
    private String purpose;

    private CreditStatus status;

    private String analystComment;


    private LocalDateTime createdAt;
}

package org.example.corporatebankingsystem.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreditRequestCreateDto {

    @NotBlank
    private String clientId;

    @NotNull
    private Double requestedAmount;

    @NotNull
    private Integer tenureMonths;

    @NotBlank
    private String purpose;
}

package org.example.corporatebankingsystem.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.corporatebankingsystem.Model.CreditStatus;

@Data
public class CreditRequestUpdateDto {

    @NotNull
    private CreditStatus status; // APPROVED / REJECTED

    @NotBlank
    private String analystComment;
}
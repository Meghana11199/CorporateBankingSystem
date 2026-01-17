package org.example.corporatebankingsystem.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor          // 🔑 needed for tests & Jackson
@AllArgsConstructor
public class ClientRequest {

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Industry is required")
    private String industry;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Primary contact name is required")
    private String primaryName;

    @Email(message = "Invalid contact email")
    private String primaryEmail;

    @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    private String primaryPhone;

    @Positive(message = "Annual turnover must be positive")
    private double annualTurnover;

    private boolean documentsSubmitted;
}

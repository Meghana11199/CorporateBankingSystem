package org.example.corporatebankingsystem.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor              // 🔑 REQUIRED
@AllArgsConstructor
public class ClientResponse {

    private String id;
    private String companyName;
    private String industry;
    private String address;
    private String primaryName;
    private String primaryEmail;
    private String primaryPhone;
    private double annualTurnover;
    private boolean documentsSubmitted;
}

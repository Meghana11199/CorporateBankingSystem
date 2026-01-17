package org.example.corporatebankingsystem.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    private String id;

    private String companyName;
    private String industry;
    private String address;

    private PrimaryContact primaryContact;

    private double annualTurnover;
    private boolean documentsSubmitted;

    private String rmId; // Relationship Manager ID from JWT
}
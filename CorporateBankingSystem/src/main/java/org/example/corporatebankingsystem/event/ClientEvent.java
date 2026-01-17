package org.example.corporatebankingsystem.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientEvent {

    private String eventType;     // CLIENT_CREATED, CLIENT_UPDATED
    private String clientId;
    private String companyName;
    private String rmId;
}
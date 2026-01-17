package org.example.corporatebankingsystem.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {

    private String eventType;   // USER_CREATED, USER_LOGIN, USER_DISABLED
    private String userId;
    private String username;
    private String role;
}

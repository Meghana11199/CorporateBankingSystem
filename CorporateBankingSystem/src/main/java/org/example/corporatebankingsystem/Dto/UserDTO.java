package org.example.corporatebankingsystem.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.corporatebankingsystem.Model.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private Role role;      // RM, ANALYST, ADMIN
    private boolean active = true;


}

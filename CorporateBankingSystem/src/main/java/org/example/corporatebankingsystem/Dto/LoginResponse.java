package org.example.corporatebankingsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.corporatebankingsystem.Model.Role;


@Data
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String role;
}

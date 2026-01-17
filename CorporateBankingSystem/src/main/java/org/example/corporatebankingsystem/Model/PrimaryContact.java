package org.example.corporatebankingsystem.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrimaryContact {
    private String name;
    private String email;
    private String phone;
}
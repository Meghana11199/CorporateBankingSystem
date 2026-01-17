package org.example.corporatebankingsystem.Config;

import org.example.corporatebankingsystem.Model.Role;
import org.example.corporatebankingsystem.Model.User;
import org.example.corporatebankingsystem.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration

public class DataInitializer {
    @Value("${admin.password}")
    private String adminPassword;
    @Bean
    CommandLineRunner initAdmin(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("admin").isEmpty()) {

                User admin = User.builder()
                        .username("admin")
                        .email("admin@bank.com")
                        .password(encoder.encode(adminPassword))
                        .role(Role.ADMIN)
                        .active(true)
                        .build();

                repo.save(admin);
            }
        };
    }
}

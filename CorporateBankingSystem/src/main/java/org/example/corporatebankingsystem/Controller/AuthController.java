package org.example.corporatebankingsystem.Controller;

import jakarta.validation.Valid;
import org.example.corporatebankingsystem.Dto.LoginRequest;
import org.example.corporatebankingsystem.Dto.LoginResponse;
import org.example.corporatebankingsystem.Model.User;
import org.example.corporatebankingsystem.Service.AuthService;
import org.example.corporatebankingsystem.Util.JwtUtil;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        String token = authService.login(
                request.getUsername(),
                request.getPassword()
        );

        return ResponseEntity.ok(
                new LoginResponse(token, jwtUtil.extractRole(token))
        );
    }


}

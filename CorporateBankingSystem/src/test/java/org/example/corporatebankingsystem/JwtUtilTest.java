package org.example.corporatebankingsystem;

import org.example.corporatebankingsystem.Util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        // inject values normally provided by application.properties
        ReflectionTestUtils.setField(jwtUtil, "secret",
                "mysecretkeymysecretkeymysecretkey12345"); // >= 32 chars
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", 3600000L);

        // manually trigger @PostConstruct
        jwtUtil.init();
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        String token = jwtUtil.generateToken("u1", "john", "ROLE_RM");

        assertNotNull(token);
        assertTrue(token.length() > 20);
    }

    @Test
    void extractUserId_shouldReturnUsername() {
        String token = jwtUtil.generateToken("u1", "john", "ROLE_RM");

        String username = jwtUtil.extractUserId(token);

        assertEquals("john", username);
    }

    @Test
    void extractRole_shouldReturnRole() {
        String token = jwtUtil.generateToken("u1", "john", "ROLE_RM");

        String role = jwtUtil.extractRole(token);

        assertEquals("ROLE_RM", role);
    }

    @Test
    void validateToken_shouldReturnTrue_forValidToken() {
        String token = jwtUtil.generateToken("u1", "john", "ROLE_RM");

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void validateToken_shouldReturnFalse_forInvalidToken() {
        boolean isValid = jwtUtil.validateToken("invalid.jwt.token");

        assertFalse(isValid);
    }
}
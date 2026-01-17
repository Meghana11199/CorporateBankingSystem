package org.example.corporatebankingsystem.ServiceTest;

import org.example.corporatebankingsystem.Model.Role;
import org.example.corporatebankingsystem.Model.User;
import org.example.corporatebankingsystem.Repo.UserRepository;
import org.example.corporatebankingsystem.Service.AuthService;
import org.example.corporatebankingsystem.Util.JwtUtil;
import org.example.corporatebankingsystem.event.UserEvent;
import org.example.corporatebankingsystem.kafka.UserEventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    // 🔴 REQUIRED (was missing earlier)
    @Mock
    private UserEventProducer userEventProducer;

    @InjectMocks
    private AuthService authService;

    // ---------------- LOGIN ----------------

    @Test
    void shouldLoginSuccessfully() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = new User();
        user.setId("u1");
        user.setUsername("john");
        user.setPassword(encoder.encode("password")); // ✅ REAL bcrypt
        user.setRole(Role.ADMIN); // use valid enum
        user.setActive(true);

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(jwtUtil.generateToken("u1", "john", "ADMIN"))
                .thenReturn("jwt-token");

        doNothing().when(userEventProducer).sendUserEvent(any(UserEvent.class));

        String token = authService.login("john", "password");

        assertEquals("jwt-token", token);
        verify(userEventProducer).sendUserEvent(any(UserEvent.class));
    }

    @Test
    void shouldThrowException_whenPasswordIsInvalid() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("$2a$10$7QJZc9mRrY1xRz8rE0kM9eZ2YkYxQ2UjXcYxFZxF7WcK8w8x5q");
        user.setActive(true);

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.login("john", "wrong-password")
        );

        assertEquals("Invalid credentials", ex.getMessage());
    }

    @Test
    void shouldThrowException_whenUserIsInactive() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("encoded");
        user.setActive(false);

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.login("john", "password")
        );

        assertEquals("User is deactivated", ex.getMessage());
    }

    // ---------------- REGISTER ----------------

    @Test
    void shouldRegisterUserSuccessfully() {
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@test.com");
        user.setPassword("password");
        user.setRole(Role.ADMIN);

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        doNothing().when(userEventProducer).sendUserEvent(any(UserEvent.class));

        User saved = authService.register(user);

        assertNotNull(saved);
        assertTrue(saved.isActive());
        assertNotEquals("password", saved.getPassword()); // encoded
        verify(userEventProducer).sendUserEvent(any(UserEvent.class));
    }

    @Test
    void shouldThrowException_whenUsernameExists() {
        User user = new User();
        user.setUsername("john");

        when(userRepository.existsByUsername("john")).thenReturn(true);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.register(user)
        );

        assertEquals("Username already exists", ex.getMessage());
    }
}

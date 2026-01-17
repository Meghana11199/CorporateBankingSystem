package org.example.corporatebankingsystem.ServiceTest;

import org.example.corporatebankingsystem.Model.Role;
import org.example.corporatebankingsystem.Model.User;
import org.example.corporatebankingsystem.Repo.UserRepository;
import org.example.corporatebankingsystem.Service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService service;

    private User buildActiveUser() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("encrypted-pass");
        user.setRole(Role.valueOf("ADMIN"));
        user.setActive(true);
        return user;
    }
    @Test
    void shouldLoadUserSuccessfully() {
        User user = buildActiveUser();

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        UserDetails userDetails =
                service.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertEquals("encrypted-pass", userDetails.getPassword());

        assertTrue(
                userDetails.getAuthorities()
                        .stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
        );

        verify(userRepository).findByUsername("admin");
    }
    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserMissing() {
        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("missing"));
    }
    @Test
    void shouldThrowExceptionWhenUserIsDeactivated() {
        User user = buildActiveUser();
        user.setActive(false);

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.loadUserByUsername("admin"));

        assertEquals("User account is deactivated", ex.getMessage());
    }
}


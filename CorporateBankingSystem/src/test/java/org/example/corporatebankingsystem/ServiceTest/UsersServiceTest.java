package org.example.corporatebankingsystem.ServiceTest;

import org.example.corporatebankingsystem.Dto.UserDTO;
import org.example.corporatebankingsystem.Model.Role;
import org.example.corporatebankingsystem.Model.User;
import org.example.corporatebankingsystem.Repo.UserRepository;
import org.example.corporatebankingsystem.Service.UsersService;
import org.example.corporatebankingsystem.kafka.UserEventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserEventProducer userEventProducer; // ✅ REQUIRED

    @InjectMocks
    private UsersService usersService;

    private UserDTO buildUserDto() {
        UserDTO dto = new UserDTO();
        dto.setUsername("rm1");
        dto.setEmail("rm1@bank.com");
        dto.setPassword("plain-pass");
        dto.setRole(Role.RM);
        dto.setActive(true);
        return dto;
    }

    private User buildUser() {
        User user = new User();
        user.setId("u1");
        user.setUsername("rm1");
        user.setEmail("rm1@bank.com");
        user.setPassword("encoded-pass");
        user.setRole(Role.RM);
        user.setActive(true);
        return user;
    }

    @Test
    void shouldReturnAllUsers() {
        when(userRepository.findAll())
                .thenReturn(List.of(buildUser()));

        List<UserDTO> users = usersService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("rm1", users.get(0).getUsername());
    }

    @Test
    void shouldCreateUserSuccessfully() {
        UserDTO dto = buildUserDto();

        when(userRepository.existsByUsername("rm1")).thenReturn(false);
        when(passwordEncoder.encode("plain-pass")).thenReturn("encoded-pass");
        when(userRepository.save(any(User.class))).thenReturn(buildUser());

        UserDTO response = usersService.createUser(dto);

        assertNotNull(response);
        assertEquals("rm1", response.getUsername());
        assertEquals(Role.RM, response.getRole());
        assertTrue(response.isActive());

        verify(passwordEncoder).encode("plain-pass");
        verify(userRepository).save(any(User.class));
        verify(userEventProducer).sendUserEvent(any()); // ✅ IMPORTANT
    }

    @Test
    void shouldFailWhenUsernameAlreadyExists() {
        UserDTO dto = buildUserDto();

        when(userRepository.existsByUsername("rm1")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usersService.createUser(dto));

        assertEquals("Username already exists", ex.getMessage());

        verify(userRepository, never()).save(any());
        verify(userEventProducer, never()).sendUserEvent(any());
    }

    @Test
    void shouldFailWhenUserNotFound() {
        when(userRepository.findByUsername("rm1"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usersService.updateUserStatus("rm1", true));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void shouldEncodePasswordBeforeSaving() {
        UserDTO dto = buildUserDto();

        when(userRepository.existsByUsername("rm1")).thenReturn(false);
        when(passwordEncoder.encode("plain-pass")).thenReturn("encoded-pass");
        when(userRepository.save(any(User.class))).thenReturn(buildUser());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        usersService.createUser(dto);

        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertEquals("encoded-pass", saved.getPassword());
    }
}
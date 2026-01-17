package org.example.corporatebankingsystem.Service;

import org.example.corporatebankingsystem.Dto.UserDTO;
import org.example.corporatebankingsystem.Model.Role;
import org.example.corporatebankingsystem.Model.User;
import org.example.corporatebankingsystem.Repo.UserRepository;
import org.example.corporatebankingsystem.event.UserEvent;
import org.example.corporatebankingsystem.kafka.UserEventProducer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEventProducer userEventProducer;

    public UsersService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserEventProducer userEventProducer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userEventProducer = userEventProducer;
    }


    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO createUser(UserDTO dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setActive(dto.isActive());

        User savedUser = userRepository.save(user);

        // 🚀 KAFKA EVENT
        userEventProducer.sendUserEvent(
                new UserEvent(
                        "USER_CREATED",
                        savedUser.getId(),
                        savedUser.getUsername(),
                        savedUser.getRole().name()
                )
        );

        return toDTO(savedUser);
    }
    public UserDTO updateUserStatus(String username, boolean active) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(active);
        User savedUser = userRepository.save(user);

        // 🚀 KAFKA EVENT
        userEventProducer.sendUserEvent(
                new UserEvent(
                        active ? "USER_ENABLED" : "USER_DISABLED",
                        savedUser.getId(),
                        savedUser.getUsername(),
                        savedUser.getRole().name()
                )
        );

        return toDTO(savedUser);
    }


    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        return dto;
    }

}

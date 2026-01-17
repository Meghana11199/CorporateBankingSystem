package org.example.corporatebankingsystem.Service;

import org.example.corporatebankingsystem.Dto.LoginRequest;
import org.example.corporatebankingsystem.Dto.LoginResponse;
import org.example.corporatebankingsystem.Dto.RegisterRequest;
import org.example.corporatebankingsystem.Model.Role;
import org.example.corporatebankingsystem.Model.User;
import org.example.corporatebankingsystem.Repo.UserRepository;
import org.example.corporatebankingsystem.Util.JwtUtil;
import org.example.corporatebankingsystem.event.UserEvent;
import org.example.corporatebankingsystem.kafka.UserEventProducer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final UserEventProducer userEventProducer;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepo,
                       JwtUtil jwtUtil,
                       UserEventProducer userEventProducer) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
        this.userEventProducer = userEventProducer;
    }

    public String login(String username, String password) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isActive()) {
            throw new RuntimeException("User is deactivated");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        userEventProducer.sendUserEvent(
                new UserEvent(
                        "USER_LOGIN",
                        user.getId(),
                        user.getUsername(),
                        user.getRole().name()
                )
        );

        return jwtUtil.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );
    }

    public User register(User user) {
        if (userRepo.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);

        User savedUser = userRepo.save(user);

        userEventProducer.sendUserEvent(
                new UserEvent(
                        "USER_REGISTERED",
                        savedUser.getId(),
                        savedUser.getUsername(),
                        savedUser.getRole().name()
                )
        );

        return savedUser;
    }
}

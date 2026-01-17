package org.example.corporatebankingsystem.Controller;

import org.example.corporatebankingsystem.Dto.UserDTO;
import org.example.corporatebankingsystem.Service.CustomUserDetailsService;
import org.example.corporatebankingsystem.Service.UsersService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")

public class AdminController {
    private final UsersService userService;

    public AdminController(UsersService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        System.out.println("Received user: " + userDTO);
        return userService.createUser(userDTO);
    }
    @PatchMapping("/users/{username}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO updateUserStatus(@PathVariable String username, @RequestParam boolean active) {
        return userService.updateUserStatus(username, active);
    }
}
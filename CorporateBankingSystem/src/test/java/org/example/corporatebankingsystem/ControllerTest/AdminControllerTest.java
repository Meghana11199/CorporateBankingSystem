package org.example.corporatebankingsystem.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.corporatebankingsystem.Config.SecurityConfig;
import org.example.corporatebankingsystem.Controller.AdminController;
import org.example.corporatebankingsystem.Dto.UserDTO;
import org.example.corporatebankingsystem.Model.Role;
import org.example.corporatebankingsystem.Service.UsersService;
import org.example.corporatebankingsystem.Util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
//@AutoConfigureMockMvc(addFilters = false)
@Import(SecurityConfig.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsersService userService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------------- GET /api/admin/users ----------------

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUsers_shouldReturnUsers_whenAdmin() throws Exception {

        UserDTO user1 = new UserDTO();
        user1.setUsername("john");
        user1.setEmail("john@mail.com");
        user1.setRole(Role.ADMIN);
        user1.setActive(true);

        UserDTO user2 = new UserDTO();
        user2.setUsername("admin");
        user2.setEmail("admin@mail.com");
        user2.setRole(Role.ADMIN);
        user2.setActive(true);

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("john"))
                .andExpect(jsonPath("$[1].role").value("ADMIN"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getUsers_shouldReturnForbidden_whenNotAdmin() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUsers_shouldReturnForbidden_whenNoAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isForbidden());
    }

    // ---------------- POST /api/admin/users ----------------

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_shouldCreateUser_whenAdmin() throws Exception {

        UserDTO request = new UserDTO();
        request.setUsername("newuser");
        request.setEmail("new@mail.com");
        request.setRole(Role.ADMIN);
        request.setActive(true);

        UserDTO response = new UserDTO();
        response.setUsername("newuser");
        response.setEmail("new@mail.com");
        response.setRole(Role.ADMIN);
        response.setActive(true);

        when(userService.createUser(any(UserDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createUser_shouldReturnForbidden_whenNotAdmin() throws Exception {

        UserDTO request = new UserDTO();
        request.setUsername("x");
        request.setEmail("x@mail.com");
        request.setRole(Role.ADMIN);
        request.setActive(true);

        mockMvc.perform(post("/api/admin/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    // ---------------- PATCH /api/admin/users/{username}/status ----------------

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUserStatus_shouldUpdateStatus_whenAdmin() throws Exception {

        UserDTO updatedUser = new UserDTO();
        updatedUser.setUsername("john");
        updatedUser.setEmail("john@mail.com");
        updatedUser.setRole(Role.ADMIN);
        updatedUser.setActive(false);

        when(userService.updateUserStatus(eq("john"), eq(false)))
                .thenReturn(updatedUser);

        mockMvc.perform(patch("/api/admin/users/john/status")
                        .param("active", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateUserStatus_shouldReturnForbidden_whenNotAdmin() throws Exception {
        mockMvc.perform(patch("/api/admin/users/john/status")
                        .param("active", "true"))
                .andExpect(status().isForbidden());
    }
}

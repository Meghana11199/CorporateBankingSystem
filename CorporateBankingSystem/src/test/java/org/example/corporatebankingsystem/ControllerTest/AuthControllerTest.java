package org.example.corporatebankingsystem.ControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.corporatebankingsystem.Controller.AuthController;
import org.example.corporatebankingsystem.Dto.LoginRequest;
import org.example.corporatebankingsystem.Service.AuthService;
import org.example.corporatebankingsystem.Util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtUtil jwtUtil;

    // ---------------- SUCCESS CASE ----------------

    @Test
    void login_shouldReturnJwtAndRole() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("password");

        String token = "fake-jwt-token";

        when(authService.login("john", "password"))
                .thenReturn(token);

        when(jwtUtil.extractRole(token))
                .thenReturn("USER");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    // ---------------- VALIDATION FAILURE ----------------

    @Test
    void login_shouldReturnBadRequest_whenRequestInvalid() throws Exception {

        // Empty body → @Valid should fail
        LoginRequest invalidRequest = new LoginRequest();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}

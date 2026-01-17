package org.example.corporatebankingsystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.corporatebankingsystem.Controller.RmController;
import org.example.corporatebankingsystem.Dto.ClientRequest;
import org.example.corporatebankingsystem.Exception.BadRequestException;
import org.example.corporatebankingsystem.Exception.ResourceNotFoundException;
import org.example.corporatebankingsystem.Exception.UnauthorizedException;
import org.example.corporatebankingsystem.Service.ClientService;
import org.example.corporatebankingsystem.Util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RmController.class)
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientService clientService;

    @MockitoBean
    private JwtUtil jwtUtil;

    // 400 - Validation error
    @Test
    @WithMockUser(roles = "RM")
    void handleValidationException() throws Exception {

        ClientRequest invalid = new ClientRequest(); // empty → validation fails

        mockMvc.perform(post("/api/rm/clients")
                        .principal(() -> "rm1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.companyName").exists());
    }

    //  404 - Resource not found
    @Test
    @WithMockUser(roles = "RM")
    void handleResourceNotFoundException() throws Exception {

        when(clientService.getClientById(any(), any()))
                .thenThrow(new ResourceNotFoundException("Client not found"));

        mockMvc.perform(get("/api/rm/clients/c1")
                        .principal(() -> "rm1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource Not Found"));
    }

    //  401 - Unauthorized
    @Test
    @WithMockUser(roles = "RM")
    void handleUnauthorizedException() throws Exception {

        when(clientService.getClientsForRm(any()))
                .thenThrow(new UnauthorizedException("Not authorized"));

        mockMvc.perform(get("/api/rm/clients")
                        .principal(() -> "rm1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"));
    }

    //  403 - Access denied
    @Test
    @WithMockUser(roles = "RM")
    void handleAccessDeniedException() throws Exception {

        when(clientService.getClientsForRm(any()))
                .thenThrow(new AccessDeniedException("Denied"));

        mockMvc.perform(get("/api/rm/clients")
                        .principal(() -> "rm1"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Forbidden"));
    }

    //  400 - Bad request
    @Test
    @WithMockUser(roles = "RM")
    void handleBadRequestException() throws Exception {

        when(clientService.getClientsForRm(any()))
                .thenThrow(new BadRequestException("Bad request"));

        mockMvc.perform(get("/api/rm/clients")
                        .principal(() -> "rm1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    //  500 - Fallback exception
    @Test
    @WithMockUser(roles = "RM")
    void handleGenericException() throws Exception {

        when(clientService.getClientsForRm(any()))
                .thenThrow(new RuntimeException("Boom"));

        mockMvc.perform(get("/api/rm/clients")
                        .principal(() -> "rm1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"));
    }
}

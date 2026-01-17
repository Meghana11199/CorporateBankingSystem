package org.example.corporatebankingsystem.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.corporatebankingsystem.Controller.RmController;
import org.example.corporatebankingsystem.Dto.ClientRequest;
import org.example.corporatebankingsystem.Dto.ClientResponse;
import org.example.corporatebankingsystem.Service.ClientService;
import org.example.corporatebankingsystem.Util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RmController.class)
@AutoConfigureMockMvc
class RmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientService clientService;

    // 🔥 REQUIRED to satisfy JwtAuthenticationFilter
    @MockitoBean
    private JwtUtil jwtUtil;
    /* ---------------- CREATE CLIENT ---------------- */

    @Test
    @WithMockUser(username = "rm1", roles = "RM")
    void createClient_shouldReturnClient() throws Exception {

        ClientRequest request = new ClientRequest();
        request.setCompanyName("ABC Corp");
        request.setIndustry("IT");
        request.setAddress("Bangalore");
        request.setPrimaryName("John");
        request.setPrimaryEmail("john@abc.com");
        request.setPrimaryPhone("9876543210");
        request.setAnnualTurnover(1_000_000);
        request.setDocumentsSubmitted(true);

        ClientResponse response = new ClientResponse();
        response.setId("c1");
        response.setCompanyName("ABC Corp");

        when(clientService.createClient(any(), any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/rm/clients")
                        .contentType(APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("c1"))
                .andExpect(jsonPath("$.companyName").value("ABC Corp"));
    }

    /* ---------------- GET ALL CLIENTS ---------------- */

    @Test
    @WithMockUser(username = "rm1", roles = "RM")
    void getClients_shouldReturnList() throws Exception {

        ClientResponse response = new ClientResponse();
        response.setId("c1");
        response.setCompanyName("ABC Corp");

        when(clientService.getClientsForRm(any()))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/rm/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("c1"))
                .andExpect(jsonPath("$[0].companyName").value("ABC Corp"));
    }

    /* ---------------- GET CLIENT BY ID ---------------- */

    @Test
    @WithMockUser(username = "rm1", roles = "RM")
    void getClient_shouldReturnClient() throws Exception {

        ClientResponse response = new ClientResponse();
        response.setId("c1");
        response.setCompanyName("ABC Corp");

        when(clientService.getClientById(eq("c1"), any()))
                .thenReturn(response);

        mockMvc.perform(get("/api/rm/clients/c1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("c1"))
                .andExpect(jsonPath("$.companyName").value("ABC Corp"));
    }

    /* ---------------- UPDATE CLIENT ---------------- */

    @Test
    @WithMockUser(username = "rm1", roles = "RM")
    void updateClient_shouldReturnUpdatedClient() throws Exception {

        ClientRequest request = new ClientRequest();
        request.setCompanyName("Updated Corp");
        request.setIndustry("Finance");
        request.setAddress("Mumbai");
        request.setPrimaryName("Alice");
        request.setPrimaryEmail("alice@corp.com");
        request.setPrimaryPhone("9876543210");
        request.setAnnualTurnover(2_000_000);
        request.setDocumentsSubmitted(true);

        ClientResponse response = new ClientResponse();
        response.setId("c1");
        response.setCompanyName("Updated Corp");

        when(clientService.updateClient(eq("c1"), any(), any()))
                .thenReturn(response);

        mockMvc.perform(put("/api/rm/clients/c1")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("c1"))
                .andExpect(jsonPath("$.companyName").value("Updated Corp"));
    }
}

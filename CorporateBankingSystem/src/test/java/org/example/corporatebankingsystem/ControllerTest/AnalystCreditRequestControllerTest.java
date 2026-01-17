package org.example.corporatebankingsystem.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.corporatebankingsystem.Config.SecurityConfig;
import org.example.corporatebankingsystem.Controller.AnalystCreditRequestController;
import org.example.corporatebankingsystem.Dto.CreditRequestResponseDto;
import org.example.corporatebankingsystem.Dto.CreditRequestUpdateDto;
import org.example.corporatebankingsystem.Model.CreditStatus;
import org.example.corporatebankingsystem.Service.CreditRequestService;
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

@WebMvcTest(AnalystCreditRequestController.class)
//@AutoConfigureMockMvc(addFilters = false)
@Import(SecurityConfig.class)
class AnalystCreditRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreditRequestService service;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtil jwtUtil;

    // ---------------- GET /pending ----------------

    @Test
    @WithMockUser(roles = "ANALYST")
    void getPending_shouldReturnPendingRequests() throws Exception {

        CreditRequestResponseDto dto =
                CreditRequestResponseDto.builder()
                        .id("1")
                        .status(CreditStatus.PENDING)
                        .build();

        when(service.getPendingRequests()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/analyst/credit-requests/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    // ---------------- GET /{id} ----------------

    @Test
    @WithMockUser(roles = "ANALYST")
    void getById_shouldReturnRequest() throws Exception {

        CreditRequestResponseDto dto =
                CreditRequestResponseDto.builder()
                        .id("1")
                        .status(CreditStatus.APPROVED)
                        .build();

        when(service.getById("1")).thenReturn(dto);

        mockMvc.perform(get("/api/analyst/credit-requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    // ---------------- GET (all) ----------------

    @Test
    @WithMockUser(roles = "ANALYST")
    void getAll_shouldReturnAllRequests() throws Exception {

        when(service.getAllRequests()).thenReturn(List.of());

        mockMvc.perform(get("/api/analyst/credit-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ---------------- PUT /{id} ----------------

    @Test
    @WithMockUser(roles = "ANALYST")
    void updateStatus_shouldUpdateSuccessfully() throws Exception {

        CreditRequestUpdateDto request = new CreditRequestUpdateDto();
        request.setStatus(CreditStatus.APPROVED);
        request.setAnalystComment("Looks good");

        CreditRequestResponseDto response =
                CreditRequestResponseDto.builder()
                        .id("1")
                        .status(CreditStatus.APPROVED)
                        .build();

        when(service.updateStatus(eq("1"), any()))
                .thenReturn(response);

        mockMvc.perform(put("/api/analyst/credit-requests/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    // ---------------- SECURITY TEST ----------------

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbidden_whenNotAnalyst() throws Exception {

        mockMvc.perform(get("/api/analyst/credit-requests"))
                .andExpect(status().isForbidden());
    }
}


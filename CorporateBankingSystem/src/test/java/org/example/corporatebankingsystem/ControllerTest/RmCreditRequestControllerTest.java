package org.example.corporatebankingsystem.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.corporatebankingsystem.Controller.RmCreditRequestController;
import org.example.corporatebankingsystem.Dto.CreditRequestCreateDto;
import org.example.corporatebankingsystem.Dto.CreditRequestResponseDto;
import org.example.corporatebankingsystem.Model.CreditStatus;
import org.example.corporatebankingsystem.Service.CreditRequestService;
import org.example.corporatebankingsystem.Util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RmCreditRequestController.class)
@AutoConfigureMockMvc(addFilters = false)
class RmCreditRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreditRequestService service;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(username = "user", roles = "RM")
    void create_shouldReturnCreatedCreditRequest() throws Exception {

        CreditRequestCreateDto request = new CreditRequestCreateDto();
        request.setClientId("client-1");
        request.setRequestedAmount(1_000_000.0);
        request.setTenureMonths(12);
        request.setPurpose("Working Capital");

        CreditRequestResponseDto response =
                CreditRequestResponseDto.builder()
                        .id("cr-1")
                        .status(CreditStatus.PENDING)
                        .build();

        // ✅ FIX IS HERE
        when(service.create(any(), any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/rm/credit-requests")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cr-1"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }



    // ---------------- GET MY REQUESTS ----------------

    @Test
    @WithMockUser(username = "user", roles = "RM")
    void getMyRequests_shouldReturnList() throws Exception {

        CreditRequestResponseDto response =
                CreditRequestResponseDto.builder()
                        .id("cr-1")
                        .status(CreditStatus.APPROVED)
                        .build();

        when(service.getForRm(any()))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/rm/credit-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("cr-1"))
                .andExpect(jsonPath("$[0].status").value("APPROVED"));
    }
}

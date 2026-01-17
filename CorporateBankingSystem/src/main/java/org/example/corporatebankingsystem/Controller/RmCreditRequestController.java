package org.example.corporatebankingsystem.Controller;

import jakarta.validation.Valid;
import org.example.corporatebankingsystem.Dto.CreditRequestCreateDto;
import org.example.corporatebankingsystem.Dto.CreditRequestResponseDto;
import org.example.corporatebankingsystem.Service.CreditRequestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rm/credit-requests")
@PreAuthorize("hasRole('RM')")
@CrossOrigin(origins = "http://localhost:4200")
public class RmCreditRequestController {

    private final CreditRequestService service;

    public RmCreditRequestController(CreditRequestService service) {
        this.service = service;
    }

    @PostMapping
    public CreditRequestResponseDto create(
            @Valid @RequestBody CreditRequestCreateDto dto,
            @AuthenticationPrincipal String rmId) {

        return service.create(dto, rmId);
    }

    @GetMapping
    public List<CreditRequestResponseDto> getMyRequests(
            @AuthenticationPrincipal String rmId) {

        return service.getForRm(rmId);
    }
}

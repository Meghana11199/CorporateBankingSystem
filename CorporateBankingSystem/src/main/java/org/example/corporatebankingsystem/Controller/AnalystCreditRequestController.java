package org.example.corporatebankingsystem.Controller;

import jakarta.validation.Valid;
import org.example.corporatebankingsystem.Dto.CreditRequestResponseDto;
import org.example.corporatebankingsystem.Dto.CreditRequestUpdateDto;
import org.example.corporatebankingsystem.Service.CreditRequestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analyst/credit-requests")
@PreAuthorize("hasRole('ANALYST')")
@CrossOrigin(origins = "http://localhost:4200")
public class AnalystCreditRequestController {

    private final CreditRequestService service;

    public AnalystCreditRequestController(CreditRequestService service) {
        this.service = service;
    }

    @GetMapping("/pending")
    public List<CreditRequestResponseDto> getPending() {
        return service.getPendingRequests();
    }
    @PutMapping("/{id}")
    public CreditRequestResponseDto updateStatus(
            @PathVariable String id,
            @Valid @RequestBody CreditRequestUpdateDto dto) {

        return service.updateStatus(id, dto);
    }
    @GetMapping("/{id}")
    public CreditRequestResponseDto getById(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping
    public List<CreditRequestResponseDto> getAll() {
        return service.getAllRequests();
    }
//    @GetMapping("/{id}")
//    public CreditRequestResponseDto getById(@PathVariable String id) {
//        return service.getById(id);
//    }


}
package org.example.corporatebankingsystem.Controller;

import jakarta.validation.Valid;
import org.example.corporatebankingsystem.Dto.ClientRequest;
import org.example.corporatebankingsystem.Dto.ClientResponse;
import org.example.corporatebankingsystem.Service.ClientService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rm/clients")
@CrossOrigin(origins = "http://localhost:4200")
public class RmController {

    private final ClientService clientService;

    public RmController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @PreAuthorize("hasRole('RM')")
    public ClientResponse createClient(@Valid @RequestBody ClientRequest dto,
                                       @AuthenticationPrincipal String rmId) {
        return clientService.createClient(dto, rmId);
    }

    @GetMapping
    @PreAuthorize("hasRole('RM')")
    public List<ClientResponse> getClients(@AuthenticationPrincipal String rmId) {
        return clientService.getClientsForRm(rmId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('RM')")
    public ClientResponse getClient(@PathVariable String id,
                                    @AuthenticationPrincipal String rmId) {
        return clientService.getClientById(id, rmId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RM')")
    public ClientResponse updateClient(@PathVariable String id,
                                       @Valid @RequestBody ClientRequest dto,
                                       @AuthenticationPrincipal String rmId) {
        return clientService.updateClient(id, dto, rmId);
    }
}

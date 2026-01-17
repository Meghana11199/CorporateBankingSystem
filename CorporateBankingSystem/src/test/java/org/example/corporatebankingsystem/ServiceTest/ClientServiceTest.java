package org.example.corporatebankingsystem.ServiceTest;

import org.example.corporatebankingsystem.Dto.ClientRequest;
import org.example.corporatebankingsystem.Dto.ClientResponse;
import org.example.corporatebankingsystem.Model.Client;
import org.example.corporatebankingsystem.Model.PrimaryContact;
import org.example.corporatebankingsystem.Repo.ClientRepository;
import org.example.corporatebankingsystem.Service.ClientService;
import org.example.corporatebankingsystem.kafka.ClientEventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientEventProducer clientEventProducer; // ✅ FIX

    @InjectMocks
    private ClientService clientService;

    private ClientRequest buildRequest() {
        ClientRequest dto = new ClientRequest();
        dto.setCompanyName("ABC Corp");
        dto.setIndustry("IT");
        dto.setAddress("Bangalore");
        dto.setPrimaryName("John");
        dto.setPrimaryEmail("john@abc.com");
        dto.setPrimaryPhone("9999999999");
        dto.setAnnualTurnover(1_000_000);
        dto.setDocumentsSubmitted(true);
        return dto;
    }

    private Client buildClient(String rmId) {
        return Client.builder()
                .id("client-1")
                .companyName("ABC Corp")
                .industry("IT")
                .address("Bangalore")
                .primaryContact(new PrimaryContact(
                        "John",
                        "john@abc.com",
                        "9999999999"
                ))
                .annualTurnover(1_000_000)
                .documentsSubmitted(true)
                .rmId(rmId)
                .build();
    }

    @Test
    void shouldCreateClientSuccessfully() {
        ClientRequest dto = buildRequest();
        Client savedClient = buildClient("rm-123");

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);
        doNothing().when(clientEventProducer).sendClientEvent(any());

        ClientResponse response = clientService.createClient(dto, "rm-123");

        assertNotNull(response);
        assertEquals("ABC Corp", response.getCompanyName());
        assertEquals("IT", response.getIndustry());
        assertEquals("John", response.getPrimaryName());

        verify(clientRepository).save(any(Client.class));
        verify(clientEventProducer).sendClientEvent(any());
    }

    @Test
    void shouldReturnClientsForRm() {
        when(clientRepository.findByRmId("rm-123"))
                .thenReturn(List.of(buildClient("rm-123"), buildClient("rm-123")));

        List<ClientResponse> responses = clientService.getClientsForRm("rm-123");

        assertEquals(2, responses.size());
        verify(clientRepository).findByRmId("rm-123");
    }

    @Test
    void shouldReturnClientByIdWhenRmMatches() {
        when(clientRepository.findById("client-1"))
                .thenReturn(Optional.of(buildClient("rm-123")));

        ClientResponse response =
                clientService.getClientById("client-1", "rm-123");

        assertEquals("ABC Corp", response.getCompanyName());
    }

    @Test
    void shouldThrowExceptionWhenRmDoesNotMatch() {
        when(clientRepository.findById("client-1"))
                .thenReturn(Optional.of(buildClient("rm-999")));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> clientService.getClientById("client-1", "rm-123"));

        assertEquals("Access denied", ex.getMessage());
    }

    @Test
    void shouldUpdateClientSuccessfully() {
        Client existingClient = buildClient("rm-123");
        ClientRequest dto = buildRequest();
        dto.setCompanyName("Updated Corp");

        when(clientRepository.findById("client-1"))
                .thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class)))
                .thenReturn(existingClient);
        doNothing().when(clientEventProducer).sendClientEvent(any());

        ClientResponse response =
                clientService.updateClient("client-1", dto, "rm-123");

        assertEquals("Updated Corp", response.getCompanyName());
        verify(clientRepository).save(existingClient);
    }

    @Test
    void shouldThrowExceptionWhenClientNotFound() {
        when(clientRepository.findById("client-1"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> clientService.updateClient("client-1", buildRequest(), "rm-123"));

        assertEquals("Client not found", ex.getMessage());
    }
}

package org.example.corporatebankingsystem.Service;

import org.example.corporatebankingsystem.Dto.ClientRequest;
import org.example.corporatebankingsystem.Dto.ClientResponse;
import org.example.corporatebankingsystem.Model.Client;
import org.example.corporatebankingsystem.Model.PrimaryContact;
import org.example.corporatebankingsystem.Repo.ClientRepository;
import org.example.corporatebankingsystem.event.ClientEvent;
import org.example.corporatebankingsystem.kafka.ClientEventProducer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepo;
    private final ClientEventProducer clientEventProducer;

    public ClientService(ClientRepository clientRepo, ClientEventProducer clientEventProducer) {
        this.clientRepo = clientRepo;
        this.clientEventProducer = clientEventProducer;
    }


    /**
     * Create a new client for the given RM ID
     */
    public ClientResponse createClient(ClientRequest dto, String rmId) {

        Client client = Client.builder()
                .companyName(dto.getCompanyName())
                .industry(dto.getIndustry())
                .address(dto.getAddress())
                .primaryContact(new PrimaryContact(
                        dto.getPrimaryName(),
                        dto.getPrimaryEmail(),
                        dto.getPrimaryPhone()
                ))
                .annualTurnover(dto.getAnnualTurnover())
                .documentsSubmitted(dto.isDocumentsSubmitted())
                .rmId(rmId)
                .build();

        Client saved = clientRepo.save(client);

        // 🚀 KAFKA EVENT
        clientEventProducer.sendClientEvent(
                new ClientEvent(
                        "CLIENT_CREATED",
                        saved.getId(),
                        saved.getCompanyName(),
                        saved.getRmId()
                )
        );

        return mapToDto(saved);
    }

    /**
     * Get all clients assigned to a specific RM
     */
    public List<ClientResponse> getClientsForRm(String rmId) {
        return clientRepo.findByRmId(rmId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific client by ID for the RM
     */
    public ClientResponse getClientById(String clientId, String rmId) {
        Client client = (Client) clientRepo.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (!client.getRmId().equals(rmId)) {
            throw new RuntimeException("Access denied");
        }

        return mapToDto(client);
    }

    /**
     * Update a client by ID for the RM
     */
    public ClientResponse updateClient(String clientId,
                                       ClientRequest dto,
                                       String rmId) {

        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (!client.getRmId().equals(rmId)) {
            throw new RuntimeException("Access denied");
        }

        client.setCompanyName(dto.getCompanyName());
        client.setIndustry(dto.getIndustry());
        client.setAddress(dto.getAddress());
        client.setPrimaryContact(new PrimaryContact(
                dto.getPrimaryName(),
                dto.getPrimaryEmail(),
                dto.getPrimaryPhone()
        ));
        client.setAnnualTurnover(dto.getAnnualTurnover());
        client.setDocumentsSubmitted(dto.isDocumentsSubmitted());

        Client updated = clientRepo.save(client);

        // 🚀 KAFKA EVENT
        clientEventProducer.sendClientEvent(
                new ClientEvent(
                        "CLIENT_UPDATED",
                        updated.getId(),
                        updated.getCompanyName(),
                        updated.getRmId()
                )
        );

        return mapToDto(updated);
    }

    /**
     * Helper method to convert Client entity to ClientResponse DTO
     */
    private ClientResponse mapToDto(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .companyName(client.getCompanyName())
                .industry(client.getIndustry())
                .address(client.getAddress())
                .primaryName(client.getPrimaryContact().getName())
                .primaryEmail(client.getPrimaryContact().getEmail())
                .primaryPhone(client.getPrimaryContact().getPhone())
                .annualTurnover(client.getAnnualTurnover())
                .documentsSubmitted(client.isDocumentsSubmitted())
                .build();
    }
}
package org.example.corporatebankingsystem.RepositoryTest;

import org.example.corporatebankingsystem.Model.Client;
import org.example.corporatebankingsystem.Repo.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void setup() {
        clientRepository.deleteAll(); // 🔥 THIS IS MANDATORY
    }

    @Test
    void findByRmId_shouldReturnClientsForGivenRmId() {
        Client client = new Client();
        client.setCompanyName("Test Corp");
        client.setIndustry("Finance");
        client.setRmId("rm1");

        clientRepository.save(client);

        List<Client> result = clientRepository.findByRmId("rm1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCompanyName()).isEqualTo("Test Corp");
    }
    @Test
    void findByRmId_shouldReturnEmptyList_whenNoClientsFound() {
        // Act
        List<Client> clients = clientRepository.findByRmId("unknownRm");

        // Assert
        assertThat(clients).isNotNull();
        assertThat(clients).isEmpty();
    }
}
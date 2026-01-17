package org.example.corporatebankingsystem.RepositoryTest;

import org.example.corporatebankingsystem.Model.CreditRequest;
import org.example.corporatebankingsystem.Model.CreditStatus;
import org.example.corporatebankingsystem.Repo.CreditRequestRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class CreditRequestRepositoryTest {

    @Autowired
    private CreditRequestRepository creditRequestRepository;

    @BeforeEach
    void cleanDatabase() {
        creditRequestRepository.deleteAll();
    }


    @AfterEach
    void tearDown() {
        creditRequestRepository.deleteAll();
    }

    @Test
    void findByRmId_shouldReturnCreditRequestsForRm() {
        // Arrange
        CreditRequest request = new CreditRequest();
        request.setRmId("rm1");
        request.setStatus(CreditStatus.PENDING);
        request.setRequestedAmount(500000.0);

        creditRequestRepository.save(request);

        // Act
        List<CreditRequest> results = creditRequestRepository.findByRmId("rm1");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getRmId()).isEqualTo("rm1");
    }

    @Test
    void findByStatus_shouldReturnCreditRequestsWithGivenStatus() {
        // Arrange
        CreditRequest approved = new CreditRequest();
        approved.setRmId("rm1");
        approved.setStatus(CreditStatus.APPROVED);
        approved.setRequestedAmount(1000000.0);

        CreditRequest rejected = new CreditRequest();
        rejected.setRmId("rm2");
        rejected.setStatus(CreditStatus.REJECTED);
        rejected.setRequestedAmount(200000.0);

        creditRequestRepository.save(approved);
        creditRequestRepository.save(rejected);

        // Act
        List<CreditRequest> approvedRequests =
                creditRequestRepository.findByStatus(CreditStatus.APPROVED);

        // Assert
        assertThat(approvedRequests).hasSize(1);
        assertThat(approvedRequests.get(0).getStatus())
                .isEqualTo(CreditStatus.APPROVED);
    }

    @Test
    void findByStatus_shouldReturnEmptyList_whenNoMatch() {
        // Act
        List<CreditRequest> results =
                creditRequestRepository.findByStatus(CreditStatus.APPROVED);

        // Assert
        assertThat(results).isEmpty();
    }
}

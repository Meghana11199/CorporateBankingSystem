package org.example.corporatebankingsystem.Repo;

import org.example.corporatebankingsystem.Model.CreditRequest;
import org.example.corporatebankingsystem.Model.CreditStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CreditRequestRepository extends MongoRepository<CreditRequest, String> {

    List<CreditRequest> findByRmId(String rmId);

    List<CreditRequest> findByStatus(CreditStatus status);
}
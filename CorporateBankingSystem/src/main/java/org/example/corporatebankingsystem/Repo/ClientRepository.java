package org.example.corporatebankingsystem.Repo;



import org.example.corporatebankingsystem.Model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;


import java.util.List;


public interface ClientRepository extends MongoRepository<Client, String> {
    List<Client> findByRmId(String rmId);
}
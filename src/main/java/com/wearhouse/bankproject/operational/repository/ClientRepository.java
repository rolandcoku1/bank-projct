package com.wearhouse.bankproject.operational.repository;
import com.wearhouse.bankproject.operational.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Override
    Optional<Client> findById(Integer integer);

    // Find client by email
    <Clients> Optional<Client> findByEmail(String email);

    // Find client by phone
    Optional<Client> findByPhone(String phone);

    @Override
    List<Client> findAll();

    // Search clients by name
    <Clients> List<Client> findByFullNameContainingIgnoreCase(String name);

    // Find clients created after a specific date
    List<Client> findByCreatedAtAfter(LocalDateTime date);

    // Find clients with accounts (custom query)
    @Query("SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.accounts WHERE c.clientId = :id")
    Optional<Client> findByIdWithAccounts(@Param("id") Integer id);

    // Find clients with loans
    @Query("SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.loans WHERE c.clientId = :id")
    Optional<Client> findByIdWithLoans(@Param("id") Integer id);

    // Count total clients
    @Query("SELECT COUNT(c) FROM Client c")
    Long countTotalClients();
}

package com.wearhouse.bankproject.operational.repository;
import com.wearhouse.bankproject.operational.entity.Clients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Clients, Integer> {

    @Override
    Optional<Clients> findById(Integer integer);

    // Find client by email
    <Clients> Optional<com.wearhouse.bankproject.operational.entity.Clients> findByEmail(String email);

    // Find client by phone
    Optional<Clients> findByPhone(String phone);

    @Override
    List<Clients> findAll();

    // Search clients by name
    <Clients> List<com.wearhouse.bankproject.operational.entity.Clients> findByFullNameContainingIgnoreCase(String name);

    // Find clients created after a specific date
    List<Clients> findByCreatedAtAfter(LocalDateTime date);

    // Find clients with accounts (custom query)
    @Query("SELECT DISTINCT c FROM Clients c LEFT JOIN FETCH c.accounts WHERE c.clientId = :id")
    Optional<Clients> findByIdWithAccounts(@Param("id") Integer id);

    // Find clients with loans
    @Query("SELECT DISTINCT c FROM Clients c LEFT JOIN FETCH c.loans WHERE c.clientId = :id")
    Optional<Clients> findByIdWithLoans(@Param("id") Integer id);

    // Count total clients
    @Query("SELECT COUNT(c) FROM Clients c")
    Long countTotalClients();
}

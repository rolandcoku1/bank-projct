package com.wearhouse.bankproject.operational.services;
import com.wearhouse.bankproject.operational.entity.Clients;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClientService {
    Clients updateClient(Integer id, Clients client);
    Clients createClient(Clients client);
    void deleteClient(Integer id);
    Optional<Clients> getClientById(Integer id);
    List<Clients> getAllClients();
    Optional<Clients> getClientByEmail(String email);
    List<Clients> searchClientsByName(String name);
    Optional<Clients> getClientWithAccounts(Integer id);
    Optional<Clients> getClientWithLoans(Integer id);
    List<Clients> getClientsCreatedAfter(LocalDateTime date);
    Long getTotalClientsCount();
}
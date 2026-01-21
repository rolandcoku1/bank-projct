package com.wearhouse.bankproject.operational.services;

import com.wearhouse.bankproject.operational.dto.ClientRequestDTO;
import com.wearhouse.bankproject.operational.dto.ClientResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClientService {
    ClientResponseDTO createClient(ClientRequestDTO dto);
    ClientResponseDTO updateClient(Integer id, ClientRequestDTO dto);
    void deleteClient(Integer id);
    Optional<ClientResponseDTO> getClientById(Integer id);
    List<ClientResponseDTO> getAllClients();
    Optional<ClientResponseDTO> getClientByEmail(String email);
    List<ClientResponseDTO> searchClientsByName(String name);
    Optional<ClientResponseDTO> getClientWithAccounts(Integer id);
    Optional<ClientResponseDTO> getClientWithLoans(Integer id);
    List<ClientResponseDTO> getClientsCreatedAfter(LocalDateTime date);
    Long getTotalClientsCount();
}

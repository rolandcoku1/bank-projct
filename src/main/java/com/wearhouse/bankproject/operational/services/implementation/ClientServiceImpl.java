package com.wearhouse.bankproject.operational.services.implementation;

import com.wearhouse.bankproject.operational.entity.Clients;
import com.wearhouse.bankproject.operational.repository.ClientRepository;
import com.wearhouse.bankproject.operational.services.ClientService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Clients createClient(@NotNull Clients client) {
        if (client.getEmail() != null && clientRepository.findByEmail(client.getEmail()).isPresent()) {
            throw new RuntimeException("Client with this email already exists");
        }
        client.setCreatedAt(LocalDateTime.now());
        return clientRepository.save(client);
    }

    @Override
    public Clients updateClient(Integer id, Clients client) {
        Clients existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        existingClient.setFullName(client.getFullName());
        existingClient.setEmail(client.getEmail());
        existingClient.setPhone(client.getPhone());
        existingClient.setAddress(client.getAddress());

        return clientRepository.save(existingClient);
    }

    @Override
    public void deleteClient(Integer id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Clients> getClientById(Integer id) {
        return clientRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Clients> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Clients> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Clients> searchClientsByName(String name) {
        return clientRepository.findByFullNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Clients> getClientWithAccounts(Integer id) {
        return clientRepository.findByIdWithAccounts(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Clients> getClientWithLoans(Integer id) {
        return clientRepository.findByIdWithLoans(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Clients> getClientsCreatedAfter(LocalDateTime date) {
        return clientRepository.findByCreatedAtAfter(date);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalClientsCount() {
        return clientRepository.countTotalClients();
    }
}
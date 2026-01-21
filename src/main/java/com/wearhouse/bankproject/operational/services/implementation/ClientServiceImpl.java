package com.wearhouse.bankproject.operational.services.implementation;

import com.wearhouse.bankproject.operational.dto.ClientRequestDTO;
import com.wearhouse.bankproject.operational.dto.ClientResponseDTO;
import com.wearhouse.bankproject.operational.dto.MapperDTO;
import com.wearhouse.bankproject.operational.entity.Client;
import com.wearhouse.bankproject.operational.repository.ClientRepository;
import com.wearhouse.bankproject.operational.services.ClientService;
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
    public ClientResponseDTO createClient(ClientRequestDTO dto) {
        Client client = MapperDTO.toClientEntity(dto);
        Client savedClient = clientRepository.save(client);
        return MapperDTO.toClientResponseDTO(savedClient);
    }

    @Override
    public ClientResponseDTO updateClient(Integer id, ClientRequestDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        client.setFullName(dto.getFullName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setAddress(dto.getAddress());

        Client updatedClient = clientRepository.save(client);
        return MapperDTO.toClientResponseDTO(updatedClient);
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
    public Optional<ClientResponseDTO> getClientById(Integer id) {
        return clientRepository.findById(id)
                .map(MapperDTO::toClientResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> getAllClients() {
        return MapperDTO.toClientResponseDTOList(clientRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClientResponseDTO> getClientByEmail(String email) {
        return clientRepository.findByEmail(email)
                .map(MapperDTO::toClientResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> searchClientsByName(String name) {
        return MapperDTO.toClientResponseDTOList(clientRepository.findByFullNameContainingIgnoreCase(name));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClientResponseDTO> getClientWithAccounts(Integer id) {
        return clientRepository.findByIdWithAccounts(id)
                .map(MapperDTO::toClientResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClientResponseDTO> getClientWithLoans(Integer id) {
        return clientRepository.findByIdWithLoans(id)
                .map(MapperDTO::toClientResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> getClientsCreatedAfter(LocalDateTime date) {
        return MapperDTO.toClientResponseDTOList(clientRepository.findByCreatedAtAfter(date));
    }

    @Override
    public Long getTotalClientsCount() {
        return clientRepository.count();
    }
}

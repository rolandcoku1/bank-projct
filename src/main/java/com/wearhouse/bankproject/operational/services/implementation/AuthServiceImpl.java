package com.wearhouse.bankproject.operational.services.implementation;

import com.wearhouse.bankproject.operational.dto.MapperDTO;
import com.wearhouse.bankproject.operational.dto.UserRequestDTO;
import com.wearhouse.bankproject.operational.dto.UserResponseDTO;
import com.wearhouse.bankproject.operational.entity.User;
import com.wearhouse.bankproject.operational.exception.DuplicateResourceException;
import com.wearhouse.bankproject.operational.exception.InvalidOperationException;
import com.wearhouse.bankproject.operational.repository.UserRepository;
import com.wearhouse.bankproject.operational.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponseDTO register(UserRequestDTO userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setRole(userRequest.getRole());

        User savedUser = userRepository.save(user);
        return MapperDTO.toUserResponseDTO(savedUser);
    }

    @Override
    public UserResponseDTO login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidOperationException("Invalid email or password"));

        if (!user.getPassword().equals(password)) {
            throw new InvalidOperationException("Invalid email or password");
        }

        return MapperDTO.toUserResponseDTO(user);
    }
}

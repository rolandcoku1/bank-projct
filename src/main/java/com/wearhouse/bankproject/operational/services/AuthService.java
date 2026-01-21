package com.wearhouse.bankproject.operational.services;


import com.wearhouse.bankproject.operational.dto.UserRequestDTO;
import com.wearhouse.bankproject.operational.dto.UserResponseDTO;

public interface AuthService {
    UserResponseDTO register(UserRequestDTO userRequest);
    UserResponseDTO login(String email, String password);
}

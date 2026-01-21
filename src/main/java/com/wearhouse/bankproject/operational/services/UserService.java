package com.wearhouse.bankproject.operational.services;

import com.wearhouse.bankproject.operational.dto.UserRequestDTO;
import com.wearhouse.bankproject.operational.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO user);

    UserResponseDTO updateUser(Integer id, UserRequestDTO user);

    void deleteUser(Integer id);

    UserResponseDTO getUserById(Integer id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserByEmail(String email);

    List<UserResponseDTO> getUsersByRole(String role);

    List<UserResponseDTO> searchUsersByName(String name);
}

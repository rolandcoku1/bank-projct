package com.wearhouse.bankproject.operational.services;
import com.wearhouse.bankproject.operational.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    User updateUser(Integer id, User user);
    void deleteUser(Integer id);
    Optional<User> getUserById(Integer id);
    List<User> getAllUsers();
    Optional<User> getUserByEmail(String email);
    List<User> getUsersByRole(String role);
    List<User> searchUsersByName(String name);
}
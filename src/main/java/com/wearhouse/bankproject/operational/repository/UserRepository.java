package com.wearhouse.bankproject.operational.repository;

import com.wearhouse.bankproject.operational.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Find user by email
    Optional<User> findByEmail(String email);

    // Find users by role
    List<User> findByRole(String role);

    // Check if email exists
    boolean existsByEmail(String email);

    // Find users by name containing (search)
    List<User> findByNameContainingIgnoreCase(String name);
}

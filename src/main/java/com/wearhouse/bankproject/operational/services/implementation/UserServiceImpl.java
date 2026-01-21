package com.wearhouse.bankproject.operational.services.implementation;
import com.wearhouse.bankproject.operational.dto.MapperDTO;
import com.wearhouse.bankproject.operational.dto.UserRequestDTO;
import com.wearhouse.bankproject.operational.dto.UserResponseDTO;
import com.wearhouse.bankproject.operational.entity.User;
import com.wearhouse.bankproject.operational.repository.UserRepository;
import com.wearhouse.bankproject.operational.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = MapperDTO.toUserEntity(dto);

        User savedUser = userRepository.save(user);
        return MapperDTO.toUserResponseDTO(savedUser);
    }

    @Override
    public UserResponseDTO updateUser(Integer id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (!user.getEmail().equals(dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(dto.getPassword());
        }

        User updatedUser = userRepository.save(user);
        return MapperDTO.toUserResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return MapperDTO.toUserResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return MapperDTO.toUserResponseDTOList(userRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return MapperDTO.toUserResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByRole(String role) {
        return MapperDTO.toUserResponseDTOList(
                userRepository.findByRole(role)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> searchUsersByName(String name) {
        return MapperDTO.toUserResponseDTOList(
                userRepository.findByNameContainingIgnoreCase(name)
        );
    }
}

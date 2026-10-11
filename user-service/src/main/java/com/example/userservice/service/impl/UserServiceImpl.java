package com.example.userservice.service.impl;

import com.example.userservice.dto.DatabaseConnectionInfoDTO;
import com.example.userservice.dto.UserRequestDTO;
import com.example.userservice.dto.UserResponseDTO;
import com.example.userservice.entity.User;
import com.example.userservice.exception.DuplicateResourceException;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Value("${server.port:8081}")
    private Integer serverPort;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id: " + id));
        return mapToDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        if (userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new DuplicateResourceException("Username '" + requestDTO.getUsername() + "' đã tồn tại!");
        }
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Email '" + requestDTO.getEmail() + "' đã tồn tại!");
        }

        User user = User.builder()
                .username(requestDTO.getUsername())
                .email(requestDTO.getEmail())
                .fullName(requestDTO.getFullName())
                .status(requestDTO.getStatus() != null ? requestDTO.getStatus() : "ACTIVE")
                .build();

        User saved = userRepository.save(user);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id: " + id));

        if (!user.getUsername().equals(requestDTO.getUsername()) && userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new DuplicateResourceException("Username '" + requestDTO.getUsername() + "' đã tồn tại!");
        }
        if (!user.getEmail().equals(requestDTO.getEmail()) && userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Email '" + requestDTO.getEmail() + "' đã tồn tại!");
        }

        user.setUsername(requestDTO.getUsername());
        user.setEmail(requestDTO.getEmail());
        user.setFullName(requestDTO.getFullName());
        if (requestDTO.getStatus() != null) {
            user.setStatus(requestDTO.getStatus());
        }

        User updated = userRepository.save(user);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id: " + id));
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public DatabaseConnectionInfoDTO getDatabaseConnectionInfo() {
        String dbName = userRepository.getCurrentDatabaseName();
        Integer pid = userRepository.getBackendPid();
        long count = userRepository.count();

        return DatabaseConnectionInfoDTO.builder()
                .serviceName("User-Service")
                .serverPort(serverPort)
                .databaseName(dbName)
                .backendPid(pid)
                .jdbcUrl(jdbcUrl)
                .connectionStatus("CONNECTED")
                .totalUsersInDb(count)
                .timestamp(LocalDateTime.now())
                .isolationProof("User-Service kết nối hoàn toàn độc lập vào database [" + dbName + "] với PostgreSQL session PID: " + pid)
                .build();
    }

    private UserResponseDTO mapToDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

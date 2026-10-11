package com.example.userservice.service;

import com.example.userservice.dto.DatabaseConnectionInfoDTO;
import com.example.userservice.dto.UserRequestDTO;
import com.example.userservice.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    UserResponseDTO createUser(UserRequestDTO requestDTO);
    UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO);
    void deleteUser(Long id);
    DatabaseConnectionInfoDTO getDatabaseConnectionInfo();
}

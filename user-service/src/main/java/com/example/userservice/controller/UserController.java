package com.example.userservice.controller;

import com.example.userservice.dto.ApiResponse;
import com.example.userservice.dto.DatabaseConnectionInfoDTO;
import com.example.userservice.dto.UserRequestDTO;
import com.example.userservice.dto.UserResponseDTO;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách người dùng thành công", users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.ok("Lấy thông tin người dùng thành công", user));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO created = userService.createUser(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo mới người dùng thành công", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO updated = userService.updateUser(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật người dùng thành công", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa người dùng thành công", null));
    }

    /**
     * API chứng minh kết nối cơ sở dữ liệu riêng biệt của User-Service
     */
    @GetMapping("/db-connection")
    public ResponseEntity<ApiResponse<DatabaseConnectionInfoDTO>> getDatabaseConnection() {
        DatabaseConnectionInfoDTO info = userService.getDatabaseConnectionInfo();
        return ResponseEntity.ok(ApiResponse.ok("Thông tin kết nối Database của User-Service", info));
    }
}

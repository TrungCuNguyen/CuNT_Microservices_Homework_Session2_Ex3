package com.example.inventoryservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSummaryDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String status;
    private LocalDateTime createdAt;
}

package com.example.userservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatabaseConnectionInfoDTO {
    private String serviceName;
    private Integer serverPort;
    private String databaseName;
    private Integer backendPid;
    private String jdbcUrl;
    private String connectionStatus;
    private Long totalUsersInDb;
    private LocalDateTime timestamp;
    private String isolationProof;
}

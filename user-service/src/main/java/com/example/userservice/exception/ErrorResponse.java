package com.example.userservice.exception;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private boolean success;
    private int status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> validationErrors;
    private LocalDateTime timestamp;
}

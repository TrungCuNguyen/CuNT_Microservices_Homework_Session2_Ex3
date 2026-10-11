package com.example.inventoryservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {
    private Long id;
    private String productName;
    private String sku;
    private BigDecimal price;
    private Integer quantity;
    private String description;
    private LocalDateTime createdAt;
}

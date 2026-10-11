package com.example.inventoryservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComparisonProofDTO {
    private String proofSummary;
    private boolean completelyIsolated;
    private DatabaseConnectionInfoDTO inventoryServiceDbInfo;
    private DatabaseConnectionInfoDTO userServiceDbInfo;
    private List<ProductResponseDTO> localProductsFromInventoryDb;
    private List<UserSummaryDTO> remoteUsersFromUserDb;
    private String conclusion;
}
